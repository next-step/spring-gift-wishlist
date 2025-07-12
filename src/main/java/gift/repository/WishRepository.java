package gift.repository;

import gift.domain.Wish;

import java.util.List;
import java.util.Optional;

public interface WishRepository {
    Wish add(Wish wish);
    List<Wish> findAllByMemberId(Long memberId);
    void delete(Long id);
    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);
}
