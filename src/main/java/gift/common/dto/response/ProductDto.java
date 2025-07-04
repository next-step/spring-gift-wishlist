package gift.common.dto.response;

import gift.domain.product.Product;

public record ProductDto(Long id, String name, Long price, String imageUrl, String state) {

    public static ProductDto from(Product p) {
        return new ProductDto(p.getId(), p.getName(), p.getPrice(), p.getImageUrl(), p.getState());
    }
}
