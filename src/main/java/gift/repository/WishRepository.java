package gift.repository;

import gift.domain.Wish;
import gift.dto.CreateWishRequest;
import gift.dto.WishResponse;

import java.util.List;
import java.util.Optional;

public interface WishRepository {

    Wish save(CreateWishRequest request);

    List<WishResponse> findAllByMember(Long memberId);

    void delete(Long wishId);

    Optional<Wish> findById(Long id);
}
