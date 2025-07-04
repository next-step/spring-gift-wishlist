// src/main/java/gift/service/ProductService.java
package gift.service;

import gift.entity.Product;
import gift.entity.Product.ValidationMode;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);

    Product createProduct(Product product, ValidationMode validationMode);

    Product updateProduct(Long id, Product product, ValidationMode validationMode);

    void deleteProduct(Long id);
}
