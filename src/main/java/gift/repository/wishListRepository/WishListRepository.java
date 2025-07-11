package gift.repository.wishListRepository;

import gift.entity.WishItem;

import java.util.List;

public interface WishListRepository {

    WishItem addWishItem(Long itemId, String itemName, String imageUrl, Integer price, Integer quantity, Long userId);

    List<WishItem> getAllWishItems(Long userId);

    List<WishItem> getWishItems(String name, Integer price, String userEmail);

    WishItem deleteItem(String name, String userEmail);

    WishItem updateWishItem(Integer quantity, Long itemId, Long userId);

    void deleteWishItem(Long userId, Long itemId);
}
