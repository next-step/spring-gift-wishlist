package giftproject.wishlist.dto;

import giftproject.gift.dto.ProductResponseDto;
import giftproject.wishlist.entity.Wish;

public record WishResponseDto(
        Long id,
        Long memberId,
        ProductResponseDto product
) {

    public WishResponseDto(Wish wish, ProductResponseDto productResponseDto) {
        this(wish.getId(), wish.getMemberId(), productResponseDto);
    }
}
