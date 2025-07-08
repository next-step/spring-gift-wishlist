package gift.product.application.port.in;

import gift.product.application.port.in.dto.ProductResponse;

public interface GetProductUseCase {
    ProductResponse getProduct(Long id);
} 