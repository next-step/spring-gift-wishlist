package gift.api.repository;

import gift.api.domain.Wishlist;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishlistRepository {

    Page<Wishlist> findWishlistByMemberId(Long memberId, Pageable pageable);

    Optional<Wishlist> findWishlistByMemberIdAndProductId(Long memberId, Long productId);

    Wishlist save(Wishlist wishlist);

    boolean deleteWishlist(Long id);
}
