package gift.wish.repository;

import gift.wish.entity.Wish;

public interface WishRepository {

    public void addWish(Wish wish);

//    public void getWishes();
//
//    public void deleteWish();

    public Wish findByWishId(Long wishId);

//    public void existsByMemberandProduct();


}
