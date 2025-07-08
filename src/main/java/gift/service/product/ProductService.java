// src/main/java/gift/service/ProductService.java
package gift.service.product;

import gift.entity.product.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);

    Product createProduct(String name, int price, String imageUrl);

    Product updateProduct(Long id, String name, int price, String imageUrl);

    void hideProduct(Long id);

    void unhideProduct(Long id);

    void deleteProduct(Long id);
}
