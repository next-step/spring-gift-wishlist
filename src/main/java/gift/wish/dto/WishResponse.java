package gift.wish.dto;

import gift.wish.entity.Wish;

public record WishResponse (Long id){
    public static WishResponse WishResponse(Wish wish) { return new WishResponse(wish.getId()); }
}
