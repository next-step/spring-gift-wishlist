package gift.wishlist;

import java.util.UUID;

public class Wishlist {
    private Long id;

    private UUID userId;

    private UUID productId;

    public Wishlist() {}

    public Wishlist(Long id, UUID userId, UUID productId) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
