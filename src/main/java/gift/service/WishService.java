package gift.service;

import gift.domain.Member;
import gift.dto.request.WishRequest;
import gift.dto.response.WishAddResponse;
import gift.dto.response.WishResponse;

import java.util.List;

public interface WishService {
    WishAddResponse add(Member member, WishRequest wishRequest);
    List<WishResponse> getWishList(Member member);
}
