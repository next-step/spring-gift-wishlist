package gift.service;

import gift.entity.Member;

public interface WishService {
    void addWish(Member member, Long productId);
}
