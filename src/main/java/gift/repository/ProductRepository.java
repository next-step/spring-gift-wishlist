package gift.repository;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    ProductResponseDto saveProduct(ProductRequestDto productRequestDto);
    Optional<ProductResponseDto> findProductById(Long id);
    List<ProductResponseDto> findAllProducts();
    void updateProduct(Long id, ProductRequestDto productRequestDto);
    void deleteProduct(Long id);

}
