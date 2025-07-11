package gift.repository;

import gift.entity.Member;
import gift.entity.WishItem;
import java.util.List;
import java.util.Optional;

public interface WishItemRepository {

    WishItem save(WishItem wishItem);

    Optional<WishItem> findByIdAndMember(Long productId, Member member);

    void deleteByItemAndMember(Long productId, Member member);

    List<WishItem> findByMember(Member member);

}
