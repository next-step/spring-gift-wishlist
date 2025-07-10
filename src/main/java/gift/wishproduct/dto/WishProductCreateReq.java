package gift.wishproduct.dto;

import java.util.UUID;

public class WishProductCreateReq {

    private UUID productId;
    private int quantity;

    public WishProductCreateReq(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    protected WishProductCreateReq() {
    }

    public UUID getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
