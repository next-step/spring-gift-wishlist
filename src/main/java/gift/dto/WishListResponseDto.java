package gift.dto;

public record WishListResponseDto(
    Long memberId,
    Long productId,
    Integer quantity
) {}
