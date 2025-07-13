package gift.product.dto;

import gift.product.entity.Product;

public record ProductResponseDto(
    Long id,
    String name,
    Integer price,
    String imageUrl
) {
    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }

    public static ProductResponseDto from(Long id, Product product) {
        return new ProductResponseDto(
            id,
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }
}
