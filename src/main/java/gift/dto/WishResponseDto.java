package gift.dto;

import gift.entity.Wish;

public record WishResponseDto(Long id, Long productId, Long quantity) {
    public WishResponseDto(Wish wish) {
        this(wish.id(), wish.productId(), wish.quantity());
    }
}
