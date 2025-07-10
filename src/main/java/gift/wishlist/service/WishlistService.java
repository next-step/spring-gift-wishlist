package gift.wishlist.service;

import gift.wishlist.dto.WishlistRequestDto;
import gift.wishlist.dto.WishlistResponseDto;

public interface WishlistService {

    WishlistResponseDto addProductToWishlist(Long memberId, WishlistRequestDto requestDto);
}
