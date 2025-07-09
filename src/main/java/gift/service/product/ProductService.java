package gift.service.product;

import gift.entity.product.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts(String role);

    Optional<Product> getProductById(Long id, String role);

    Product createProduct(String name, int price, String imageUrl, String role);

    Product updateProduct(Long id, String name, int price, String imageUrl, String role);

    void deleteProduct(Long id, String role);

    void hideProduct(Long id);

    void unhideProduct(Long id);
}
