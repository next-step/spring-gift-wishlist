package gift.wish.service;

import gift.member.entity.Member;
import gift.wish.dto.WishCreateRequestDto;
import gift.wish.dto.WishCreateResponseDto;
import gift.wish.dto.WishPageResponseDto;

public interface WishService {

    public WishCreateResponseDto addWish(Member member, WishCreateRequestDto wishCreateRequestDto);

    public WishPageResponseDto getWishes(Member member, Integer page, Integer size);

    public void deleteWish(Member member, Long wishId);

}
