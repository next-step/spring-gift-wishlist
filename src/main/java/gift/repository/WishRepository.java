package gift.repository;

import gift.entity.Wish;

public interface WishRepository {
    Wish createWish(Wish newWish);
}