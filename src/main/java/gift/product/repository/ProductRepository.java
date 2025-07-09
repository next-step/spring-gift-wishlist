package gift.product.repository;

import gift.product.entity.Product;
import java.util.List;

public interface ProductRepository {

    void saveProduct(Product product);

    List<Product> findAllProducts();

    Product findProductById(Long productId);

    void updateProductById(Product product);

    void deleteProductById(Long productId);
}
