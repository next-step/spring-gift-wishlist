package gift.dto;

import jakarta.validation.constraints.NotNull;

public class WishRequestDto {

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    public Long getProductId() { return productId; }
}