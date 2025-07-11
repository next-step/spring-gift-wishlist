package gift.repository;

import gift.entity.Wish;

import java.util.List;

public interface WishRepository {
    Wish createWish(Wish wish);
    List<Wish> findAllWishByMemberId(Long memberId);
    boolean existsWishById(Long id);
    boolean existsWishByMemberIdAndProductId(Long memberId, Long productId);
    void deleteWishById(Long id);
}

