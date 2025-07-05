package gift.dto.product;

import gift.domain.Product;


public class ProductMapper {

    public static Product toEntity(ProductRequest request) {
        return Product.of(request.id(), request.name(), request.price(), request.imageUrl());
    }

    public static ProductResponse toResponse(Product product) {
        return ProductResponse.of(product.getName(), product.getPrice(), product.getImageUrl());
    }

}
