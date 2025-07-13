package gift.repository.wish;

import gift.entity.Wish;
import java.util.List;
import java.util.Optional;

public interface WishRepository {

  List<Wish> findByMemberId(Long memberId);

  Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);

  Wish createWish(Wish wish);

  int updateQuantity(Long memberId, Wish wish);

  int deleteAllWish(Long memberId);

  int deleteByProductId(Long memberId, Long productId);
}
