package gift.wish.repository;

import gift.member.entity.Member;
import gift.wish.entity.Page;
import gift.wish.entity.Wish;
import java.util.List;

public interface WishRepository {

    public void addWish(Wish wish);

    public List<Wish> getWishes(Member member, Page page);

    public void deleteWish(Long wishId);

    public Wish findByWishId(Long wishId);

    public Long countWishesByMemberId(Long memberId);

    public Boolean existsByMemberAndProduct(Long memberId, Long productId);
}
