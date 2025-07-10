package gift.entity;

public class Wish {
    private Long id;
    private Long productId;
    private Long memberId;
    private Long quantity;

    public Wish(Long id, Long productId, Long memberId, Long quantity) {
        this.id = id;
        this.productId = productId;
        this.memberId = memberId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getQuantity() {
        return quantity;
    }
}