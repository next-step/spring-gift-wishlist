package gift.service;

import gift.domain.Member;
import gift.dto.request.WishRequest;
import gift.dto.response.WishMsgResponse;
import gift.dto.response.WishResponse;

import java.util.List;

public interface WishService {
    WishMsgResponse add(Member member, WishRequest wishRequest);
    List<WishResponse> getWishList(Member member);
    WishMsgResponse deleteByProductId(Member member, Long productId);
}
