package gift.product.application.port.in;

import gift.product.application.port.in.dto.ProductRequest;

public interface UpdateProductUseCase {
    void updateProduct(Long id, ProductRequest request);
} 