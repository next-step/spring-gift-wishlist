package gift.dto;

import gift.entity.Product;
import gift.entity.Wish;

public record WishResponseDto(
        Long id,
        Long productId,
        String productName,
        int productPrice
) {
    public static WishResponseDto of(Wish wish, Product product) {
        return new WishResponseDto(
                wish.getId(),
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}
