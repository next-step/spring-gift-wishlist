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

		WishList wishList = new WishList(userId, request.itemId());
		return wishListRepository.save(wishList);
	}


	public List<WishList> getWishList(Long userId) {
		return wishListRepository.findAll(userId);
	}

	public void deleteWishList(Long userId, Long itemId) {
		wishListRepository.delete(userId, itemId);
	}
}
