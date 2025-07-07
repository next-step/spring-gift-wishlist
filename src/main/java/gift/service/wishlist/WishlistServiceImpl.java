package gift.service.wishlist;

import gift.dto.api.wishlist.WishlistRequestDto;
import gift.dto.api.wishlist.WishlistResponseDto;
import org.springframework.stereotype.Service;

@Service
public class WishlistServiceImpl implements WishlistService {
    
    @Override
    public WishlistResponseDto addToMyWishlist(Long userId, WishlistRequestDto requestDto) {
        return null;
    }
}
