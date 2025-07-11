package gift.service;

import gift.dto.WishResponseDto;

import java.util.List;

public interface WishService {
    WishResponseDto createWish(Long memberId, Long productId);
    List<WishResponseDto> findAllWishesByMemberId(Long memberId);
    void deleteWish(Long memberId, Long wishId);
}
