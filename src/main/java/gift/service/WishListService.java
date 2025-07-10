package gift.service;

import gift.dto.WishListResponseDto;

import java.util.List;

public interface WishListService {

    List<WishListResponseDto> getWishListByMemberId(Long memberId);
}
