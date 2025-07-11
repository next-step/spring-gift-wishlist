package gift.repository;

import gift.dto.PageRequestDto;
import gift.dto.PageResult;
import gift.entity.Product;

import java.util.Optional;

public interface ProductRepository {
    Product createProduct(Product product);

    PageResult<Product> findAllProducts(PageRequestDto pageRequestDto);

    Optional<Product> findProductById(Long id);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
