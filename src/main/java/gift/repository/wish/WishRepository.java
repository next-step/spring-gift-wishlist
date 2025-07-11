package gift.repository.wish;

import gift.entity.Wish;
import java.util.List;
import java.util.Optional;

public interface WishRepository {

  List<Wish> findByMemberId(Long memberId);

  Wish createWish(Wish wish);

  Optional<Wish> updateQuantity(Long memberId, Wish wish);

  int deleteByMemberId(Long memberId);
}
