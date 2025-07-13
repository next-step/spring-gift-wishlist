package gift.repository;

import gift.entity.Product;
import gift.entity.Wish;
import gift.misc.Pair;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WishRepository {

    Wish createWish(Wish newWish);

    List<Pair<Wish, Product>> findMemberWishes(Long memberId);

    Optional<Wish> findMemberWishByProductId(Long productId, Long memberId);

    Wish updateMemberWishQuantityByProductId(Long quantity, Long productId, Long memberId);

    void deleteMemberWishByProductId(Long productId, Long memberId);
}