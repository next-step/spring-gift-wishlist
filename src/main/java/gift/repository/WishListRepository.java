package gift.repository;

import gift.domain.WishList;
import gift.repository.projection.WishListWithProduct;
import java.util.List;
import java.util.Optional;

public interface WishListRepository {

    WishList save(WishList product);

    void update(Long id, WishList updatedProduct);

    void deleteAllByIds(List<Long> ids);

    int deleteByIdAndMemberId(Long id, Long memberId);

    List<WishList> findAll();

    Optional<WishList> findById(Long id);

    List<WishListWithProduct> findAllWithProductByMemberId(Long userId);

    Optional<WishList> findByMemberIdAndProductId(Long memberId, Long aLong);
}
