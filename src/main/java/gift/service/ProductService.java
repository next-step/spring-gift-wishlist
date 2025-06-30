package gift.service;

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
    Product patch(Product product); // 부분 업데이트 메소드 추가
    void deleteById(Long productId); // 삭제 메소드 추가
}
