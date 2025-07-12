package gift.dto.response;

public record WishResponse(
        Long id,
        Long productId,
        String productName,
        int price,
        String imageUrl
) {}
