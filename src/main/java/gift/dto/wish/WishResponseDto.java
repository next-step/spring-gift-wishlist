package gift.dto.wish;

import gift.entity.Wish;

public class WishResponseDto {

  private Long id;
  private Long productId;
  private String productName;
  private Long quantity;

  public WishResponseDto(Long id, Long productId, String productName, Long quantity) {
    this.id = id;
    this.productId = productId;
    this.productName = productName;
    this.quantity = quantity;
  }

  public WishResponseDto(Wish wish) {
    this(wish.getId(), wish.getMemberId(), null, wish.getQuantity());
  }

  public Long getId() {
    return id;
  }

  public String getProductName() {
    return productName;
  }

  public Long getProductId() {
    return productId;
  }

  public Long getQuantity() {
    return quantity;
  }
}
