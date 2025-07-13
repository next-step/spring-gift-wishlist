package gift.repository;

import gift.dto.CreateWishResponse;

import java.util.Optional;

public interface WishRepository {
    CreateWishResponse saveWish(Long memberId, Long productId, int quantity);

    Optional<CreateWishResponse> findProductById(Long memberId, Long productId);
}
