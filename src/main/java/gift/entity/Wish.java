package gift.entity;

public class Wish {
    final private Long memberId;
    final private Long productId;
    private int quantity;

    public Wish(Long memberId, Long productId, int quantity) {
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Long getMemberId() { return memberId; }
    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
}
