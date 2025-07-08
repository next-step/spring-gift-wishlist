package gift.dto.product;

import gift.entity.Product;

public record ProductDefaultResponse(
    Long id,
    String name,
    Long price,
    String imageUrl
) {

    public static ProductDefaultResponse from(Product product) {
        return new ProductDefaultResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }
}
