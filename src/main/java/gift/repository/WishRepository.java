package gift.repository;

import gift.domain.Wish;
import gift.dto.CreateWishRequest;

public interface WishRepository {

    Wish save(CreateWishRequest request);
}
