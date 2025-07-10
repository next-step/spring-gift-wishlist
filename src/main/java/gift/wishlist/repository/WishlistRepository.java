package gift.wishlist.repository;

import gift.wishlist.Wishlist;
import java.util.List;

public interface WishlistRepository {

    Wishlist save(Wishlist wishlist);

    List<Wishlist> findAllByOrderByCreatedAtDesc();
}