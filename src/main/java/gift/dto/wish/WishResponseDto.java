package gift.dto.wish;

import gift.entity.Wish;

public class WishResponseDto {

  private Long id;
  private Long memberId;
  private Long productId;
  private Long quantity;

  public WishResponseDto(Long id, Long memberId, Long productId, Long count) {
    this.id = id;
    this.memberId = memberId;
    this.productId = productId;
    this.quantity = count;
  }

  public WishResponseDto(Wish wish) {
    this(wish.getId(), wish.getMemberId(), wish.getProductId(), wish.getQuantity());
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
}
