package gift.wishlist.service;

import gift.wishlist.dto.WishlistRequestDto;
import gift.wishlist.dto.WishlistResponseDto;
import gift.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultWishlistService implements WishlistService {

    private final WishlistRepository wishlistRepository;

    public DefaultWishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public WishlistResponseDto addProductToWishlist(Long memberId, WishlistRequestDto requestDto) {
        return wishlistRepository.addProductToWishlist(memberId, requestDto);
    }
}
