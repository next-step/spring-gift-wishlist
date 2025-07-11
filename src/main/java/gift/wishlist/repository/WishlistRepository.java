package gift.wishlist.repository;

import gift.wishlist.entity.Wish;
import java.util.List;

public interface WishlistRepository {
    List<Wish> findAllByMemberId(Long memberId);
    long save(Wish wish);
    void update(Long id, int amount);
    void delete(Long memberId, Long productId);
}
