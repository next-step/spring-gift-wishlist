package gift.wishlist.entity;

import gift.wishlist.vo.Amount;

public class Wish {
    private Long id;
    private Long memberId;
    private Long productId;
    private Amount amount;

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Amount getAmount() {
        return amount;
    }

    public Wish(Long id, Long memberId, Long productId, Amount amount) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.amount = amount;
    }

    public Wish(Long id, Wish wish) {
        this.id = id;
        this.memberId = wish.getMemberId();
        this.productId = wish.getProductId();
        this.amount = wish.getAmount();
    }
}
