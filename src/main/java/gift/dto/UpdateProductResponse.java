package gift.dto;

import gift.domain.Product;

public record UpdateProductResponse(Long id, String name, Long price, String imageUrl) {

    public static UpdateProductResponse from(Product product) {
        return new UpdateProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }
}
