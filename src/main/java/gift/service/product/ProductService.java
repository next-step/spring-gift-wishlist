package gift.service.product;

import gift.model.CustomPage;
import gift.entity.Product;

import java.util.List;

public interface ProductService {
    @Deprecated
    List<Product> getAll();
    CustomPage<Product> getBy(int page, int size);
    Product getById(Long productId);
    Product create(Product product);
    Product update(Product product);
    Product patch(Product product);
    void deleteById(Long productId);
}
