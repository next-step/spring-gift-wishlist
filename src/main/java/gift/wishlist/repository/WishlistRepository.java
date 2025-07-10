package gift.wishlist.repository;

import gift.wishlist.dto.WishlistRequestDto;
import gift.wishlist.dto.WishlistResponseDto;

public interface WishlistRepository {

    WishlistResponseDto addProductToWishlist(Long memberId, WishlistRequestDto requestDto);
}
