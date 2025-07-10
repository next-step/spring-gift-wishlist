package gift.repository;

import gift.entity.WishlistItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository {
    int addWishlistItem(WishlistItem item);

    WishlistItem findByIdOrElseThrow(Long id);

    List<WishlistItem> findAllWishlistItemsByMemberId(Long memberId);

    int updateWishlistItemById(Long id, Long quantity);

    int deleteById(Long id);
}
