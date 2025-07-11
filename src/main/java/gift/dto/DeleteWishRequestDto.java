package gift.dto;

import jakarta.validation.constraints.NotNull;

public record DeleteWishRequestDto(
        @NotNull(message = "상품 아이디를 입력해 주세요")
        Long productId
) {

}
