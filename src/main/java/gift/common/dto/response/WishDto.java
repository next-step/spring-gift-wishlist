package gift.common.dto.response;

import gift.domain.wish.Wish;

public record WishDto(Long id, Long productId, Integer quantity) {
    public static WishDto from(Wish wish) {
        return new WishDto(wish.getId(), wish.getProductId(), wish.getQuantity());
    }
}
