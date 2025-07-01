package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {

    ProductResponseDto saveProduct(ProductRequestDto requestDto);

    List<ProductResponseDto> findAllProducts();

    ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto);

    void deleteProduct(Long id);


}
