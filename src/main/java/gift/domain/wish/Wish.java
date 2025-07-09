package gift.domain.wish;

public class Wish {
    private final Long id;
    private final Long memberId;
    private final Long productId;
    private final Long quantity;

    private Wish(Long id, Long memberId, Long productId, Long quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static Wish of(Long id, Long memberId, Long productId, Long quantity) {
        return new Wish(id, memberId, productId, quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
