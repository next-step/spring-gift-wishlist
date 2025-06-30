package gift.repository;

import gift.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(Long productId);
    Product save(Product product);
    Product updateNameById(Long productId, String name);
    Product updatePriceById(Long productId, Long price);
    Product updateImageUrlById(Long productId, String imageUrl);
    Boolean deleteById(Long productId); // 삭제 메소드 추가
}
