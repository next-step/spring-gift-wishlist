package gift.repository;


import gift.dto.ProductRequestDto;
import gift.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAll();

    Long create(Product product);

    Optional<Product> findById(Long productId);

    int update(Long productId, ProductRequestDto requestDto);

    void delete(Long productId);
}
