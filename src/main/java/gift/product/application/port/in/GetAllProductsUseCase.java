package gift.product.application.port.in;

import gift.common.pagination.Page;
import gift.common.pagination.Pageable;
import gift.product.application.port.in.dto.ProductResponse;

public interface GetAllProductsUseCase {
    Page<ProductResponse> getProducts(Pageable pageable);
} 