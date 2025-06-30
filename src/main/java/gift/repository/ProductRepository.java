package gift.repository;

import gift.model.CustomPage;
import gift.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.logging.log4j.util.ProcessIdUtil;

public interface ProductRepository {
    List<Product> findAll();
    CustomPage<Product> findAll(int page, int size);
    Optional<Product> findById(Long productId);
    Product save(Product product);
    Product updateFieldById(Long productId, Consumer<Product> updater);
    Boolean deleteById(Long productId); // 삭제 메소드 추가
}
