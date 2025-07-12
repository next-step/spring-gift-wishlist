package gift.repository;

import java.util.Optional;

public interface WishRepository {

    long addProduct(String productName, String email);

    Optional<Integer> getCurrnetQuantity(String productName, Long userId);
}
