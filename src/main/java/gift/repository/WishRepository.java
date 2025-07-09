package gift.repository;

import gift.domain.Wish;

import java.util.List;

public interface WishRepository {
    void save(Wish wish);
    void updateQuantity(Long memberId, Long productId, int quantity);
    void delete(Long memberId, Long productId);
    List<Wish> findWishByMemberId(Long memberId);
    boolean exists(Long memberId, Long productId);
}

