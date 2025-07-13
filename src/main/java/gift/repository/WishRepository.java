package gift.repository;

import gift.dto.WishWithProductDto;
import gift.entity.Wish;

import java.util.List;
import java.util.Optional;

public interface WishRepository {
    Wish saveWish(Wish wish);
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
    List<WishWithProductDto> findByMemberIdWithProduct(Long memberId);
    void deleteWish(Long id);
    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);
}
