package gift.wishproduct.repository;

import gift.domain.WishProduct;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WishProductRepository {

    WishProduct save(WishProduct wishProduct);

    Optional<WishProduct> findById(UUID id);

    List<WishProduct> findByMemberId(UUID memberId);

    List<WishProduct> findByProductId(UUID productId);

    Optional<WishProduct> findByMemberIdAndProductId(UUID memberId, UUID productId);

    void deleteById(UUID id);

    void update(WishProduct wishProduct);

    void deleteAll();

}
