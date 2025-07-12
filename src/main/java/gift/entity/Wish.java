package gift.entity;

import gift.dto.WishRequestDto;

public record Wish(Long id, Long userId, Long productId, Long quantity) {
    public Wish(WishRequestDto wishRequestDto) {
        this(null, null, wishRequestDto.productId(), wishRequestDto.quantity());
    }
}
