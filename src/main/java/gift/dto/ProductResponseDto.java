package gift.dto;

import gift.entity.Product;

public record ProductResponseDto(Long id, String name, Integer price, String imageUrl, Product.Status status) {
    public ProductResponseDto(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getStatus());
    }
}
