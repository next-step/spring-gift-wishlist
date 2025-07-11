package gift.service;

import gift.dto.CreateWishRequestDto;
import gift.dto.WishResponseDto;
import java.util.List;

public interface WishService {
    WishResponseDto createWish(CreateWishRequestDto requestDto, Long memberId);
    List<WishResponseDto> findMemberWishes(Long memberId);
    WishResponseDto updateMemberWishQuantityByProductId(Long quantity, Long productId, Long memberId);
    void deleteMemberWishByProductId(Long productId, Long memberId);
}