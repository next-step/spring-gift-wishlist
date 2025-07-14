package gift.dto;

public class CreateWishRequest {

    Long productId;
    Long memberId;

    public CreateWishRequest(Long productId, Long memberId) {
        this.productId = productId;
        this.memberId = memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
