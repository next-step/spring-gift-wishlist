package gift.repository;

import gift.entity.WishList;

import java.util.List;

public interface WishListRepository {

    List<WishList> getWishListByMemberId(Long memberId);

    void addWishList(Long memberId ,Long productId, Integer quantity);

    boolean isWishListExistByMemberIdAndWishListId(Long memberId ,Long wishListId);

    void deleteWishList(Long wishListId);
}
