package gift.wish.repository;

import gift.wish.entity.Wish;

import java.util.List;
import java.util.Optional;

public interface WishRepository {
    Wish createWish(Wish wish);
    List<Wish> findAllWishByMemberId(Long memberId);
    Optional<Wish> findWishById(Long id);
    boolean existsWishByMemberIdAndProductId(Long memberId, Long productId);
    void deleteWishById(Long id);
}

