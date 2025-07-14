package gift.service;

import gift.dto.WishListRequestDto;
import gift.dto.WishListResponseDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface WishListService {

    List<WishListResponseDto> getWishListByMemberId(Long memberId);

    void addWishList(Long memberId, WishListRequestDto wishListRequestDto);

    void deleteWishList(Long memberId, Long wishListId);

    void validateWishListByMemberIdAndWishListId(Long memberId, Long wishListId);
}
