package gift.wishlist;

import java.util.UUID;

public class WishlistSaveRequestDto {

    private UUID productId;

    public WishlistSaveRequestDto(UUID productId) {
        this.productId = productId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }
}
