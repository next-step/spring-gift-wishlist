package gift.dto;

public class WishRequest {
    private final Long productId;

    public WishRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

}
