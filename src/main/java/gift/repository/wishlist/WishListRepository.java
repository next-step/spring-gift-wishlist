package gift.repository.wishlist;

import gift.dto.wishlist.WishRequestDto;
import gift.entity.Wish;
import java.util.List;

public interface WishListRepository {
    public Wish create(Wish wish);

    List<Wish> findAll(Long memberId);
}
