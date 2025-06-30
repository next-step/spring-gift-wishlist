package gift.repository;

import gift.entity.Product;
import java.util.List;

public interface ProductRepository {
    long createProduct(Product product);
    List<Product> findAllProducts();
    Product findProductById(Long id);
    void updateProduct(Long id, Product product);
    void deleteProduct(Long id);
}
