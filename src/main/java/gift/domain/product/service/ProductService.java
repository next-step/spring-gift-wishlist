package gift.domain.product.service;


import gift.common.pagination.Page;
import gift.common.pagination.Pageable;
import gift.domain.product.model.Product;

public interface ProductService {

    Page<Product> getAllProducts(Pageable pageable);

    Product getProductById(Long id);

    Product addProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(Long id);


}
