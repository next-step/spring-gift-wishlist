// src/main/java/gift/service/ProductService.java
package gift.service;

import gift.entity.Product;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public interface ProductService {

    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);

    Product createProduct(Product product, BiFunction<Product, String, Product> productCreator);

    Product updateProduct(Long id, Product product,
            BiFunction<Product, String, Product> updateProduct);

    void deleteProduct(Long id);
}
