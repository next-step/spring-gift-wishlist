package gift.service;

import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;
import gift.dto.ProductResponseDto;
import gift.dto.WishResponse;

import java.util.List;
import java.util.Optional;

public interface WishService {
    CreateWishResponse create(Long memberId, CreateWishRequest request);

    List<WishResponse> findAllWishes(Long memberId);

    void deleteWish(Long memberId, Long wishId);
}
