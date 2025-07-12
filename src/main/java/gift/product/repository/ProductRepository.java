package gift.product.repository;

import gift.common.pagination.PageRequestDto;
import gift.common.pagination.PageResult;
import gift.product.entity.Product;

import java.util.Optional;

public interface ProductRepository {
    Product createProduct(Product product);

    PageResult<Product> findAllProducts(PageRequestDto pageRequestDto);

    Optional<Product> findProductById(Long id);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
