package gift.repository.wishlist;

import gift.dto.api.wishlist.WishlistResponseDto;
import gift.entity.WishlistInfo;
import java.util.List;

public interface WishlistRepository {
    
    WishlistInfo addToMyWishlist(Long id, Long id1, Long productCnt);
    
    List<WishlistInfo> findMyWishlistByUserId(Long id);
    
    void deleteFromMyWishlist(Long id, Long productId);
    
    WishlistInfo checkMyWishlist(Long id, Long id1);
    
    WishlistInfo modifyProductCntFromMyWishlist(Long userId, Long productId, Long productCnt);
}
