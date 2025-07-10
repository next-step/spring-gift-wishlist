package gift.wishlist.repository;

import gift.wishlist.Wishlist;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository {

    Wishlist save(Wishlist wishlist);

    List<Wishlist> findAllByOrderByCreatedAtDesc();

    Optional<Wishlist> findById(Long id);

    void remove(Long id);
}