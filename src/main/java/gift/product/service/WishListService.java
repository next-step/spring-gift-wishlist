package gift.product.service;


import gift.product.dto.CreateWishListRequest;
import gift.product.entity.WishList;
import gift.product.repository.WishListRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WishListService {

	private final WishListRepository wishListRepository;
	public WishListService(WishListRepository wishListRepository) {
		this.wishListRepository = wishListRepository;
	}

	public Long createWishList(Long userId, CreateWishListRequest request) {
		WishList wishList = new WishList(userId, request.itemId());
		return wishListRepository.save(wishList);
	}


	public List<WishList> getWishList(Long userId) {
		return wishListRepository.findAll(userId);
	}

}
