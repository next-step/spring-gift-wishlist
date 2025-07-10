package gift.repository;

import gift.domain.Wish;

import java.util.List;

public interface WishRepository {

    Wish addWish(Long memberId, Long productId);

    boolean isWished(Long memberId, Long productId);

    List<Wish> getWishlistByMemberId(Long memberId);

    void removeByMemberIdAndWishId(Long memberId, Long wishId);

}
