package gift.repository;

import gift.domain.WishSummary;
import gift.domain.WishList;

import java.util.List;

public interface WishListRepository {
    void save(WishList wishList);

    List<WishSummary> findAllWishSummaryByMemberId(Long memberId);
}
