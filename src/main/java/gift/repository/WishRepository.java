package gift.repository;

import gift.entity.Wish;
import java.util.List;

public interface WishRepository {
    Wish createWish(Wish newWish);
    List<Wish> findMemberWishes(Long memberId);
}