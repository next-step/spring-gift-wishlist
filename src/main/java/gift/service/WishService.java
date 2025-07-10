package gift.service;

import gift.dto.CreateWishRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.WishResponseDto;
import java.util.List;

public interface WishService {
    WishResponseDto createWish(CreateWishRequestDto requestDto, Long memberId);
    List<WishResponseDto> findMemberWishes(Long memberId);
}