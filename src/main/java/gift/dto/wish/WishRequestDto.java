package gift.dto.wish;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WishRequestDto {

  @NotNull(message = "상품 번호는 필수입니다.")
  private Long productId;

  @NotNull(message = "상품 개수는 필수입니다")
  @Min(1)
  private Long quantity;

  public WishRequestDto(Long productId, Long count) {
    this.productId = productId;
    this.quantity = count;
  }

  public Long getProductId() {
    return productId;
  }

  public Long getQuantity() {
    return quantity;
  }
}
