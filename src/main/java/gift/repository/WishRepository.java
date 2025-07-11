package gift.repository;

import gift.entity.Wish;
import java.util.List;

public interface WishRepository {

    Wish save(Wish wish);

    List<Wish> findByMemberId(Long memberId);

    boolean deleteByMemberIdAndProductId(Long memberId, Long productId);
}
