package gift.entity;

public class Wish {
    private Long id;
    private Long memberId;
    private Long productId;
    private Integer quantity;

    // 연관관계 객체 (조회용으로 쓰는거)
    private Member member;
    private Product product;

    public Wish() {}

    public Wish(Long id, Long memberId, Long productId, Integer quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Wish(Long memberId, Long productId, Integer quantity) {
        this(null, memberId, productId, quantity);
    }

    // Getter/Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
} 