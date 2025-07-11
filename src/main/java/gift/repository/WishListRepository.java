package gift.repository;

import gift.domain.WishSummary;
import gift.domain.Wish;

import java.util.List;

public interface WishListRepository {
    void saveWish(Wish wish);

    List<WishSummary> findAllWishSummaryByMemberId(Long memberId);

    void deleteWish(Wish wish);
}
