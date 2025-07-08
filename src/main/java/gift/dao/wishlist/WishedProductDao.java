package gift.dao.wishlist;

import gift.entity.WishedProduct;
import java.util.List;
import java.util.Optional;

public interface WishedProductDao {
    List<WishedProduct> findAllProduct(Long userId);
    List<WishedProduct> findAllProduct(Long userId, int page, int size);
    Optional<WishedProduct> findById(Long userId, Long productId);
    Integer addProduct(Long userId, Long productId, Integer quantity);
    Integer removeProduct(Long userId, Long productId);
    Integer updateProduct(Long userId, Long productId, Integer quantity);
    Integer increaseProductQuantity(Long userId, Long productId, Integer quantity);
    Integer decreaseProductQuantity(Long userId, Long productId, Integer quantity);
    Integer clear(Long userId);
    Integer count(Long userId);
    Integer totalPrice(Long userId);
    Integer totalQuantity(Long userId);
}
