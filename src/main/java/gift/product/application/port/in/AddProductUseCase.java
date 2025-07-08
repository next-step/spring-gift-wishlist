package gift.product.application.port.in;

import gift.product.application.port.in.dto.ProductRequest;
import gift.product.application.port.in.dto.ProductResponse;

public interface AddProductUseCase {
    ProductResponse addProduct(ProductRequest request);
} 