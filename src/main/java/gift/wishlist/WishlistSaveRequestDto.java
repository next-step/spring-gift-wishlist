package gift.wishlist;

import java.util.UUID;

public class WishlistSaveRequestDto {

    private UUID userId;

    private UUID productId;

    public WishlistSaveRequestDto(UUID userId, UUID productId) {
        this.userId = userId;
        this.productId = productId;
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
}
