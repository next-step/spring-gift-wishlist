package gift.service;

import gift.domain.Member;
import gift.dto.request.WishRequest;
import gift.dto.response.WishResponse;

public interface WishService {
    WishResponse add(Member member, WishRequest wishRequest);
}
