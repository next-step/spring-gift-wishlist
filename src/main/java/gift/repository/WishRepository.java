package gift.repository;

import gift.entity.Wish;

public interface WishRepository {
    Wish saveWish(Wish wish);
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
}
