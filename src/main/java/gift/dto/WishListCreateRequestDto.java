package gift.dto;

import jakarta.validation.constraints.Min;

public record WishListCreateRequestDto(
    Long productId,
    @Min(value = 1, message = "상품 추가 개수는 최소 1개 이상이며 기존 개수에 더해집니다.")
    Integer quantity
) {}
