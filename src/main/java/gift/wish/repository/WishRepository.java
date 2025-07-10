package gift.wish.repository;

import gift.member.entity.Member;
import gift.wish.entity.Wish;
import java.util.List;

public interface WishRepository {

    public void addWish(Wish wish);

    public List<Wish> getWishes(Member member, Integer size, Integer offset);

    public void deleteWish(Long wishId);

    public Wish findByWishId(Long wishId);

    public Long countWishesByMemberId(Long memberId);

//    public void existsByMemberandProduct();


}
