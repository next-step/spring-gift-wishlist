package gift.service;

import gift.dto.WishListResponseDto;
import gift.entity.Member;

public interface WishService {
    void addWish(Member member, Long productId);
    WishListResponseDto getWishList(Member member);
}
