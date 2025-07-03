package gift.dto;

import gift.entity.Product;

public record ProductResponse(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        Boolean validated
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getValidated()
        );
    }
}