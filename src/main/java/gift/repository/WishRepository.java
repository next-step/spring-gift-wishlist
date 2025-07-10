package gift.repository;

import gift.entity.Product;
import gift.entity.Wish;
import java.util.List;
import java.util.Optional;

public interface WishRepository {
    Wish createWish(Wish newWish);
    List<Wish> findMemberWishes(Long memberId);
    Optional<Wish> findMemberWishByProductId(Long productId, Long memberId);
}