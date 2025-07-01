package gift.repository;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

  List<ProductResponseDto> findAllProduct();

  Optional<ProductResponseDto> findProductById(Long id);

  ProductResponseDto createProduct(ProductRequestDto requestDto);

  Optional<ProductResponseDto> updateProduct(Long id, ProductRequestDto requestDto);

  void deleteProduct(Long id);

}
