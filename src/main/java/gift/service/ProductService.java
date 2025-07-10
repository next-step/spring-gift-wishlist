package gift.service;

import gift.dto.request.ProductRequest;
import gift.dto.request.ProductUpdateRequest;
import gift.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse register(ProductRequest request);

    ProductResponse getProduct(Long productId);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> searchByName(String keyword);

    ProductResponse updateProduct(Long productId, ProductUpdateRequest request);

    void deleteProduct(Long productId);
}