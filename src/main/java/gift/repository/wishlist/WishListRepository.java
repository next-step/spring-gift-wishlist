package gift.repository.wishlist;

import gift.dto.wishlist.WishRequestDto;
import gift.entity.Wish;

public interface WishListRepository {
    public Wish create(Wish wish);
}
