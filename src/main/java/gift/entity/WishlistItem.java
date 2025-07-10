package gift.entity;

public record WishlistItem(Long id, Long memberId, Long productId, Long quantity) {
    public WishlistItem() {
        this(null, null, null, null);
    }
}
