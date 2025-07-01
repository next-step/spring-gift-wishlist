package gift.repository;

import gift.dto.ProductResponseDto;
import gift.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAllProducts();
    Optional<Product> findProductById(Long id);
    Product findProductByIdElseThrow(Long id);
    Product saveProduct(String name, Long price, String imageUrl);
    int updateProduct(Long id, String name, Long price, String imageUrl);
    int deleteProduct(Long id);
}
