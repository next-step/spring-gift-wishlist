package gift.dto;

public record WishResponse(
    ProductResponseDto product,
    int quantity
) {
}
