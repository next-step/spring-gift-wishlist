package gift.service;

import gift.dto.CreateWishRequestDto;
import gift.dto.WishResponseDto;

public interface WishService {
    WishResponseDto createWish(CreateWishRequestDto requestDto, Long memberId);
}