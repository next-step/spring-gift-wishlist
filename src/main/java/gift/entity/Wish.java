package gift.entity;

public class Wish {

    private Long userId;
    private final Long productId;
    private final int quantity;

    public Wish(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
