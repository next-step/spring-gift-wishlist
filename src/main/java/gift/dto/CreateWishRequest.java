package gift.dto;

public record CreateWishRequest(Long memberId, Long productId, int quantity) {
}
