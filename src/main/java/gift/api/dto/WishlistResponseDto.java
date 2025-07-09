package gift.api.dto;

import gift.api.domain.Product;
import gift.api.domain.Wishlist;

public record WishlistResponseDto(
        Long id,
        ProductResponseDto product
) {

    public static WishlistResponseDto of(Wishlist wishlist, Product product) {
        return new WishlistResponseDto(
                wishlist.getId(),
                ProductResponseDto.from(product)

        );
    }
}
