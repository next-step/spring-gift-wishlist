package gift.dto.wishlist;

import jakarta.validation.constraints.NotNull;

public record WishlistRequestDto(
        @NotNull(message = "상품 ID는 필수입니다.")
        Long productId
) {
}