package gift.dto;

public class CreateWishResponse {

    Long wishId;
    Long productId;

    public CreateWishResponse(Long wishId, Long productId) {
        this.wishId = wishId;
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}