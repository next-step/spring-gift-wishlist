package gift.repository;

import gift.domain.Product;
import gift.dto.common.Page;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    void update(Long id, Product updatedProduct);

    void deleteAllByIds(List<Long> ids);

    void deleteById(Long id);

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Page<Product> findAllByPage(int pageNumber, int pageSize);
}
