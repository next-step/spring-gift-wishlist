package gift.service;

import gift.dto.WishListRequestDto;
import gift.dto.WishListResponseDto;

import java.util.List;

public interface WishListService {

    List<WishListResponseDto> getWishListByMemberId(Long memberId);

    void addWishList(Long memberId, WishListRequestDto wishListRequestDto);
}
