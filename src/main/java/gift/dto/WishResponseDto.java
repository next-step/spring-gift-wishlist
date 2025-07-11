package gift.dto;

public record WishResponseDto (
    Long wishlistId,
    Long productId,
    String productName,
    long price,
    String imageUrl
) {
}
