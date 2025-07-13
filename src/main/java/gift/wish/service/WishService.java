package gift.wish.service;

import gift.wish.dto.WishResponseDto;

import java.util.List;

public interface WishService {
    WishResponseDto createWish(Long memberId, Long productId);
    List<WishResponseDto> findAllWishesByMemberId(Long memberId);
    void deleteWish(Long memberId, Long wishId);
}
