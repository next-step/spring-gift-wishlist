package gift.service;

import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;

import java.util.List;

public interface WishService {

    List<WishResponseDto> findWishList(Long memberId);
    WishResponseDto saveWish(Long memberId, WishRequestDto dto);
    void deleteWish(Long wishId, Member member);
}
