package gift.repository;

import gift.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void updateStatus(long id, Product.Status status);
    Long saveProduct(String name, Integer price, String imageUrl, Product.Status status);
    Optional<Product> findProductById(Long id);
    void deleteProductById(Long id);
    void updateProduct(Product product);
    List<Product> findAllProducts();
}
