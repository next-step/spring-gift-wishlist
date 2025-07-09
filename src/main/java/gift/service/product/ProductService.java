package gift.service.product;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import java.util.List;

public interface ProductService {

  List<ProductResponseDto> findAllProduct();

  ProductResponseDto findProductById(Long id);

  ProductResponseDto createProduct(ProductRequestDto requestDto);

  ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto);

  void deleteProduct(Long id);

}
