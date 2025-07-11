package gift.product.repository;


import gift.product.entity.WishList;

import java.util.List;


public interface WishListRepository {
	List<WishList> findAll(Long userId);
	Long save(WishList wishList);
	void delete(Long userId, Long itemId);
}
