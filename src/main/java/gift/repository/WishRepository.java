package gift.repository;

import gift.entity.WishProduct;
import java.util.List;
import java.util.Optional;

public interface WishRepository {

    long addProduct(String productName, String email);

    Optional<Integer> getCurrnetQuantity(String productName, Long userId);

    List<WishProduct> getWishList(String email);

    int deleteProduct(Long wishId, String productName);

    int updateWish(Long wishId, String productName, int quantity);

}
