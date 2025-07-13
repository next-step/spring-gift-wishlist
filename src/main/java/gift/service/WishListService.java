package gift.service;

import gift.dto.WishListCreateRequestDto;
import gift.dto.WishListResponseDto;
import gift.dto.WishListUpdateRequestDto;
import java.util.List;

public interface WishListService {

  WishListResponseDto addToWishList(Long memberId, WishListCreateRequestDto wishListCreateRequestDto);

  List<WishListResponseDto> getWishList(Long memberId);

  WishListResponseDto updateQuantity(Long memberId, WishListUpdateRequestDto wishListUpdateRequestDto);

  void removeFromWishList(Long memberId, Long productId);

  void clearWishList(Long memberId);
}
