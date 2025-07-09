package gift.domain;

public class Wishlist {
    private Long id;
    private Long userId;
    private Long productId;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }

    public Wishlist(Long id, Long userId, Long productId) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
    }
}
