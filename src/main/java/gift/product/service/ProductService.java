package gift.product.service;

import gift.common.pagination.PageRequestDto;
import gift.common.pagination.PageResult;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    PageResult<ProductResponseDto> findAllProducts(PageRequestDto pageRequestDto);

    ProductResponseDto findProductById(Long id);

    ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);

    void deleteProduct(Long id);
}
