package gift.repository;

import gift.domain.Wish;

import java.util.List;

public interface WishRepository {
    Wish add(Wish wish);
    List<Wish> findAllByMemberId(Long memberId);
}
