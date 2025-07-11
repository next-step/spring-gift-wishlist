package gift.entity;

public class Wish {

    private Long id;
    private Long productId;
    private Long memberId;
    private int quantity;

    public Wish() {}

    public static Wish of(Long productId, Long memberId, int quantity) {
        return new Wish(null, productId, memberId, quantity);
    }

    public static Wish withId(Long id, Long productId, Long memberId, int quantity) {
        return new Wish(id, productId, memberId, quantity);
    }

    private Wish(Long id, Long productId, Long memberId, int quantity) {
        this.id = id;
        this.productId = productId;
        this.memberId = memberId;
        this.quantity = quantity;
    }

    public Long getId() {return id;}

    public Long getProductId() {return productId;}

    public Long getMemberId() {return memberId;}

    public int getQuantity() {return quantity;}


}
