package gift.repository;

import gift.entity.Wish;
import java.util.List;

public interface WishRepository {

    Long saveWish(Wish wish);

    List<Wish> findAllWishes(Long memberId);
}
