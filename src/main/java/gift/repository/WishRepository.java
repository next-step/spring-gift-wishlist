package gift.repository;

import gift.entity.Wish;
import java.util.List;
import java.util.Optional;

public interface WishRepository {

    Long saveWish(Wish wish);

    List<Wish> findAllWishes(Long memberId);

    void deleteWish(Long wishId);

    Optional<Wish> findWish(Long wishId);
}
