package gift.dto;

import gift.domain.WishItem;

public record WishResponse(
    Long productId,
    Long price,
    String name,
    String imageUrl,
    Long quantity
) {
    public static WishResponse from(WishItem item) {
        return new WishResponse(
            item.getProductId(),
            item.getPrice(),
            item.getName(),
            item.getImageUrl(),
            item.getQuantity()
        );
    }
}
