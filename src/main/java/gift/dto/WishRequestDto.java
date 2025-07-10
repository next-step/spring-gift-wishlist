package gift.dto;

public class WishRequestDto {

    private final Long productId;

    public WishRequestDto(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
