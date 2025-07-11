package gift.entity;

public class Wish {

    private Long id;
    private Long memberId;
    private Long productId;
    private int quantity;


    public Wish(Long id, Long memberId, Long productId, int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }


    public Wish() {
    }
}
