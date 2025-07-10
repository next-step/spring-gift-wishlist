package gift.wishlist.dto;

public record WishResponseDto(
        Long id,
        Long productId,
        String name,
        Long price,
        String imageUrl,
        int quantity
) {
}
