package gift.repository.wishlist;

import gift.entity.WishlistInfo;
import java.util.List;

public interface WishlistRepository {
    
    WishlistInfo addToMyWishlist(Long id, Long id1, Long productCnt);
    
    List<WishlistInfo> findMyWishlistByUserId(Long id);
}
