package gift.wishlist.dto;

public record WishRequestDto(
        Long productId,
        int quantity
) {
}
