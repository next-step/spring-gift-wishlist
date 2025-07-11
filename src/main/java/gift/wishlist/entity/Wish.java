package gift.wishlist.entity;

public class Wish {
    private Long id;
    private Long memberId;
    private Long productId;
    private int amount;

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getAmount() {
        return amount;
    }

    public Wish(Long id, Long memberId, Long productId, int amount) {
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
