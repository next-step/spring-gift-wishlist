package gift.repository;

import gift.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAllProducts();

    Product findProductByIdOrElseThrow(Long id);

    Product createProduct(String name, Long price, String imageUrl);

    void deleteProduct(Long id);

    void updateProduct(Product product);
}
