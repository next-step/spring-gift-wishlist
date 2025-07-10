package gift.wish.service;

import gift.member.entity.Member;
import gift.wish.dto.WishCreateRequestDto;
import gift.wish.dto.WishCreateResponseDto;

public interface WishService {

    public WishCreateResponseDto addWish(Member member, WishCreateRequestDto wishCreateRequestDto);

//    public void getWishes();
//
//    public void deleteWish();

}
