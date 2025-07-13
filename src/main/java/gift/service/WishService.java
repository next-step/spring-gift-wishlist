package gift.service;

import gift.dto.request.WishRequest;
import gift.dto.response.WishAddResponse;
import gift.dto.response.WishMsgResponse;
import gift.dto.response.WishResponse;

import java.util.List;

public interface WishService {
    WishAddResponse add(Long memberId, WishRequest wishRequest);
    List<WishResponse> getWishList(Long memberId);
    WishMsgResponse deleteByProductId(Long memberId, Long productId);
}
