package gift.product.dto;

import gift.product.entity.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        String imgUrl
) {
    public static ProductResponse from(Product product) {
        return product.toResponse();
    }
}