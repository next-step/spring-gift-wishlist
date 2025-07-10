package gift.repository;

import gift.domain.Wish;
import gift.dto.CreateWishRequest;
import gift.dto.WishResponse;

import java.util.List;

public interface WishRepository {

    Wish save(CreateWishRequest request);

    List<WishResponse> findAllByMember(Long memberId);
}
