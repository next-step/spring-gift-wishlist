package gift.domain.wish;

public class Wish {

    private Long id;
    private Long memberId;
    private Long productId;
    private int quantity;

    protected Wish() {
    }

    public Wish(Long memberId, Long productId, int quantity) {
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Wish(Long id, Long memberId, Long productId, int quantity) {
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

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void update(int quantity){
        this.quantity = quantity;
    }
}
