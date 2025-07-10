package gift.repository;
import gift.entity.Wish;
import java.util.List;
import java.util.Optional;

public interface WishRepository {

    List<Wish> findAllWishes(Long memberId);
    Optional<Wish> findWishById(Long wishId);
    Wish saveWish(Long memberId, Long productId);
    int deleteWish(Long wishId);
    boolean isInWishList(Long memberId, Long productId);
}

