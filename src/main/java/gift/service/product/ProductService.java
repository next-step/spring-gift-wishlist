package gift.service.product;

import gift.entity.Product;
import java.util.List;

public interface ProductService {

  List<Product> findAllProduct();

  Product findProductById(Long id);

  Product createProduct(Product requestDto);

  Product updateProduct(Long id, Product requestDto);

  void deleteProduct(Long id);

}
