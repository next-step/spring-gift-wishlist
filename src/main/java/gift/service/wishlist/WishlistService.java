package gift.service.wishlist;

import gift.dto.api.wishlist.WishlistRequestDto;
import gift.dto.api.wishlist.WishlistResponseDto;
import java.util.List;

public interface WishlistService {
    
    WishlistResponseDto addToMyWishlist(Long userId, WishlistRequestDto requestDto);
    
    List<WishlistResponseDto> findMyWishlistByUserId(Long userId);
    
    void deleteFromMyWishlist(Long userId, WishlistRequestDto requestDto);
}
