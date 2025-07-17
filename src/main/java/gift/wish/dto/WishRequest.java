package gift.wish.dto;

import jakarta.validation.constraints.NotNull;

public record WishRequest(
        @NotNull(message = "상품 ID는 비어있을 수 없습니다.")
        Long productId
) {
}