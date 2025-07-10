package gift.wish.repository;

import gift.member.entity.Member;
import gift.wish.entity.Page;
import gift.wish.entity.Wish;
import java.util.List;

public interface WishRepository {

    void addWish(Wish wish);

    List<Wish> getWishes(Member member, Page page);

    void deleteWish(Long wishId);

    Wish findByWishId(Long wishId);

    Long countWishesByMemberId(Long memberId);

    Boolean existsByMemberAndProduct(Long memberId, Long productId);
}
