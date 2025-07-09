package gift.dto;

import gift.domain.Wish;

public class WishResponse {
    private final Long id;
    private final Long productId;
    private final int quantity;

    public WishResponse(Long id, Long productId, int quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static WishResponse from(Wish wish) {
        return new WishResponse(
                wish.getId(),
                wish.getProductId(),
                wish.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

}


