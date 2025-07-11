package gift.dto;

import jakarta.validation.constraints.NotNull;

public record AddWishItemRequest (
        @NotNull(message = "상품 ID는 제시되어야 합니다.")
        Long productId
) {
}
