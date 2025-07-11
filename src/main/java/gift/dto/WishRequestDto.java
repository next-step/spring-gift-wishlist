package gift.dto;

import gift.entity.Wish;

public class WishRequestDto {

    private final Long productId;
    private final int quantity;

    public WishRequestDto(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Wish toEntity() {
        return new Wish(productId, quantity);
    }
}
