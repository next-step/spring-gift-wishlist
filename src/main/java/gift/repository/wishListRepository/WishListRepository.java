package gift.repository.wishListRepository;

import gift.entity.WishItem;

public interface WishListRepository {

    WishItem addWishItem(String name, Integer quantity, String userEmail);
}
