package gift.product.repository;


import gift.product.entity.WishList;

import java.util.List;
import java.util.Optional;


public interface WishListRepository {
	List<WishList> findAll(Long userId);
	Long save(WishList wishList);
	void delete(WishList wishList);
}
