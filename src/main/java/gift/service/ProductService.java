package gift.service;

import gift.dto.request.ProductCreateRequestDto;
import gift.dto.request.ProductUpdateRequestDto;
import gift.dto.response.ProductCreateResponseDto;
import gift.dto.response.ProductGetResponseDto;
import java.util.List;

public interface ProductService {

    ProductCreateResponseDto saveProduct(ProductCreateRequestDto productCreateRequestDto);

    List<ProductGetResponseDto> findAllProducts();

    ProductGetResponseDto findProductById(Long productId);

    void updateProductById(Long productId, ProductUpdateRequestDto productUpdateRequestDto);

    void deleteProductById(Long productId);
}
