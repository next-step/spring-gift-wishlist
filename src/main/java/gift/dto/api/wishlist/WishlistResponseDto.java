package gift.dto.api.wishlist;

public class WishlistResponseDto {
    private String productName;
    private Long productCnt;
    
    public WishlistResponseDto(String productName, Long productCnt) {
        this.productName = productName;
        this.productCnt = productCnt;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public Long getProductCnt() {
        return productCnt;
    }
}
