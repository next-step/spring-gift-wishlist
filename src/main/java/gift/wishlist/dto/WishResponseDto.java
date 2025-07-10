package gift.wishlist.dto;

import gift.wishlist.model.Wish;

public record WishResponseDto(
        Long id,
        Long productId,
        int quantity
) {
    public static WishResponseDto from(Wish wish) {
        return new WishResponseDto(
                wish.getId(),
                wish.getProductId(),
                wish.getQuantity()
        );
    }

}
