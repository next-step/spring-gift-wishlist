package gift.wishlist.dto;

import gift.product.dto.ProductItemDto;
import gift.wishlist.entity.WishlistItem;
import java.time.LocalDateTime;

public record WishlistItemDto(
        ProductItemDto product,
        int quantity,
        LocalDateTime addedAt
) {

    public WishlistItemDto(WishlistItem wishlistItem) {
        this(new ProductItemDto(wishlistItem.getProduct()),
                wishlistItem.getQuantity(), wishlistItem.getAddedAt());
    }

}
