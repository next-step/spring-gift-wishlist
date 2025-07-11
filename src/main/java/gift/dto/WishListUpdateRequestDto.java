package gift.dto;

import jakarta.validation.constraints.Min;

public record WishListUpdateRequestDto(
    Long productId,
    @Min(value = 0, message = "개수 수정 요청은 0개 이상만 가능 (0개시 삭제 처리)")
    Integer quantity
) {}
