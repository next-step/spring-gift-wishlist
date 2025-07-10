package gift.dto;

public record CreateWishRequestDto(
        Long productId,
        Long quantity
) {

}