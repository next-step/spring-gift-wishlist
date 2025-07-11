package gift.dto;

public class WishResponseDto {
    private Long productId;
    private String productName;

    public WishResponseDto(Long productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }
}
