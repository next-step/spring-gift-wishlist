package gift.service;

import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;

import java.util.Optional;

public interface WishService {
    CreateWishResponse create(Long memberId, CreateWishRequest request);
}
