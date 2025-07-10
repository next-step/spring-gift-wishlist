package gift.wishlist.entity;

public class Wishlist {
    private Long id;
    private Long memberId;
    private Long productId;

    public Wishlist(Long id, Long memberId, Long productId) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
    }

    public Wishlist(Long memberId, Long productId){
        this.memberId = memberId;
        this.productId = productId;
    }

    public Long getId() {return this.id;}
    public Long getMemberId() {return this.memberId;}
    public Long getProductId() {return this.productId;}
}
