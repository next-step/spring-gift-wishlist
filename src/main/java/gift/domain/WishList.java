package gift.domain;

public record WishList(Long id, Long memberId, Long productId, int quantity) {

    public static WishList withId(Long id, Long memberId, Long productId, int quantity) {
        return new WishList(id, memberId, productId, quantity);
    }

    public static WishList withoutId(Long memberId, Long productId, int quantity) {
        return new WishList(null, memberId, productId, quantity);
    }
}
