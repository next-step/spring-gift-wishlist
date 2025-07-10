package gift.dto;

import jakarta.validation.constraints.Positive;

public record CreateWishRequest(Long memberId, Long productId, @Positive(message = "수량은 0보다 커야합니다.") int quantity) {
}
