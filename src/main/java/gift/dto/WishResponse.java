package gift.dto;

import gift.domain.Wish;

import java.time.LocalDateTime;

public record WishResponse(
        Long wishId,
        Long productId,
        LocalDateTime createdDate
) {
    public static WishResponse from(Wish wish) {
        return new WishResponse(
                wish.getId(),
                wish.getProductId(),
                wish.getCreatedDate()
        );
    }
}
