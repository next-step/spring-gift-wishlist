package gift.repository;

import gift.entity.Wish;
import java.util.List;

public interface WishRepository {

    Wish save(Wish wish);

    List<Wish> findByMemberId(Long memberId);

    void deleteByMemberIdAndProductId(Long memberId, Long productId);
}
