package gift.service;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProduct(Long id);

    ProductResponse update(Long id, ProductRequest request);

    void delete(Long id);
}
