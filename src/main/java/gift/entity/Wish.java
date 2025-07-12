package gift.entity;

public class Wish {

  private Long id;
  private Long memberId;
  private Long productId;
  private Long quantity;

  public Wish(Long id, Long memberId, Long productId, Long count) {
    this.id = id;
    this.memberId = memberId;
    this.productId = productId;
    this.quantity = count;
  }

  public Wish(Long memberId, Long productId, Long count) {
    this(null, memberId, productId, count);
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

  public void setId(Long id) {
    this.id = id;
  }
}
