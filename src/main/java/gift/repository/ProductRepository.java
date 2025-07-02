package gift.repository;

import gift.dto.ProductResponseDto;
import gift.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAllProducts();
    Optional<Product> findProductById(Long id);
    Product findProductByIdElseThrow(Long id);
    Product saveProduct(String name, Long price, String imageUrl, Boolean approved);
    int updateProduct(Long id, String name, Long price, String imageUrl, Boolean approved);
    int deleteProduct(Long id);
}
