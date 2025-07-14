package gift.entity;

public class Wish {

    private Long id;
    private Long productId;
    private Long memberId;

    public static Wish of(Long id, Long productId, Long memberId) {
        return new Wish(id, productId, memberId);
    }

    private Wish(Long id, Long productId, Long memberId) {
        this.id = id;
        this.productId = productId;
        this.memberId = memberId;
    }

    public Long getId() {return id;}

    public Long getProductId() {return productId;}

    public Long getMemberId() {return memberId;}


}
