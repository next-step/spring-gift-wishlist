package gift.repository.product;

import gift.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

  List<Product> findAllProduct();

  Optional<Product> findProductById(Long id);

  Product createProduct(Product product);

  Optional<Product> updateProduct(Long id, Product product);

  int deleteProduct(Long id);

}
