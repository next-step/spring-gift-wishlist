package gift.dto.api;

public record WishResponseDto(
        Long productId,
        String name,
        int price,
        String imageUrl,
        int quantity
) { }
