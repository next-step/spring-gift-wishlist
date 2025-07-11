package gift.repository;

import gift.dto.WishWithProductDto;
import gift.entity.Wish;

import java.util.List;

public interface WishRepository {
    Wish saveWish(Wish wish);
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
    List<WishWithProductDto> findByMemberIdWithProduct(Long memberId);
}
