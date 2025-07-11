package gift.wish.domain;

public class Wish {
    private Long id;
    private Long memberId;
    private Long productId;
    private Integer quantity;

    public Wish(Long id, Long memberId, Long productId, Integer quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }
}
