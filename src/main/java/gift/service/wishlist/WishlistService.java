package gift.service.wishlist;

import gift.dto.api.wishlist.WishlistRequestDto;
import gift.dto.api.wishlist.WishlistResponseDto;

public interface WishlistService {
    
    WishlistResponseDto addToMyWishlist(Long userId, WishlistRequestDto requestDto);
}
