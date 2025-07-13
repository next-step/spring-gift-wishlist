package gift.repository;

import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;

import java.util.List;
import java.util.Optional;

public interface WishRepository {
    CreateWishResponse saveWish(Long memberId, Long productId, int quantity);

    Optional<CreateWishResponse> findProductById(Long memberId, Long productId);

    List<CreateWishRequest> findAllWishesByMemberId(Long memberId);

    void deleteWish(Long memberId, Long wishId);
}
