package gift.product.service;


import gift.product.dto.CreateWishListRequest;
import gift.product.entity.WishList;
import gift.product.repository.WishListRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class WishListService {

	private final WishListRepository wishListRepository;
	public WishListService(WishListRepository wishListRepository) {
		this.wishListRepository = wishListRepository;
	}

	public Long createWishList(Long userId, CreateWishListRequest request) {
		if(wishListRepository.findByUserIdAndItemId(userId, request.itemId()).isPresent())
			throw new DataIntegrityViolationException("이미 위시리스트에 담은 상품입니다");

		WishList wishList = new WishList(userId, request.itemId(), 1);
		return wishListRepository.save(wishList);
	}

	public void increaseAmount(Long userId, Long itemId) {
		wishListRepository.increaseAmount(userId, itemId);
	}

	public void decreaseAmount(Long userId, Long itemId) {
		WishList wishList = wishListRepository.findByUserIdAndItemId(userId, itemId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 위시상품입니다."));

		if(wishList.getAmount() == 1) {
			throw new IllegalArgumentException("최소 상품 수량은 1개입니다.");
		}
		wishListRepository.decreaseAmount(userId, itemId);
	}

	public List<WishList> getWishList(Long userId) {
		return wishListRepository.findAll(userId);
	}

	public void deleteWishList(Long userId, Long itemId) {
		wishListRepository.delete(userId, itemId);
	}
}
