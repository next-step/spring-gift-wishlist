package gift.repository.wishListRepository;

import gift.entity.WishItem;

import java.util.List;

public interface WishListRepository {

    WishItem addWishItem(Long itemId, String itemName, String imageUrl, Integer price, Integer quantity, Long userId);

    List<WishItem> getAllWishItems(Long userId);

    WishItem updateWishItem(Integer quantity, Long itemId, Long userId);

    void deleteWishItem(Long userId, Long itemId);
}
