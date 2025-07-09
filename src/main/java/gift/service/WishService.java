package gift.service;

import gift.domain.Wish;
import gift.repository.WishRepository;

import java.util.List;

public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public List<Wish> getWishes(Long memberId) {
        return wishRepository.findWishByMemberId(memberId);
    }
}
