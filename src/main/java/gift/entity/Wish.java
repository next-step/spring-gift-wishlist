package gift.entity;

public class Wish {
    Long productId;
    Long memberId;

    public Wish(Long productId, Long memberId) {
        this.productId = productId;
        this.memberId = memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
