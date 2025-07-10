package gift.repository.wishListRepository;

import gift.entity.WishItem;

import java.util.List;

public interface WishListRepository {

    WishItem addWishItem(String name, Integer quantity, String userEmail);

    List<WishItem> getAllWishItems(String userEmail);

    List<WishItem> getWishItems(String name, Integer price, String userEmail);

    WishItem deleteItem(String name, String userEmail);
}
