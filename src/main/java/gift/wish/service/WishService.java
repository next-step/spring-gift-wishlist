package gift.wish.service;

import gift.member.entity.Member;
import gift.wish.dto.WishCreateRequestDto;
import gift.wish.dto.WishCreateResponseDto;
import gift.wish.dto.WishGetRequestDto;
import gift.wish.dto.WishPageResponseDto;

public interface WishService {

    WishCreateResponseDto addWish(Member member, WishCreateRequestDto wishCreateRequestDto);

    WishPageResponseDto getWishes(Member member, WishGetRequestDto wishGetRequestDto);

    void deleteWish(Member member, Long wishId);

}
