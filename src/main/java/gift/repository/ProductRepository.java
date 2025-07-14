package gift.repository;

import gift.entity.Product;
import java.util.List;
import java.util.Optional;


public interface ProductRepository {

    Optional<Product> findById(long productId);

    void createProduct(Product product);

    void updateProduct(Product product);

    void delete(long productId);

    boolean productExists(long id);

    List<Product> findAll();

    Optional<Product> findByName(String productName);

}
