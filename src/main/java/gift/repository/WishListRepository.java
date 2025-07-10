package gift.repository;

import gift.domain.WishSummary;
import gift.domain.Wish;

import java.util.List;

public interface WishListRepository {
    void save(Wish wish);

    List<WishSummary> findAllWishSummaryByMemberId(Long memberId);
}
