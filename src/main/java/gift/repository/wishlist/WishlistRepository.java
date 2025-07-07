package gift.repository.wishlist;

import gift.entity.WishlistInfo;

public interface WishlistRepository {
    
    WishlistInfo addToMyWishlist(Long id, Long id1, Long productCnt);
}
