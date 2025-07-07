package gift.dto.api.wishlist;

public class WishlistRequestDto {
    private Long productId;
    private Long productCnt;
    
    public WishlistRequestDto(Long productId, Long productCnt) {
        this.productId = productId;
        this.productCnt = productCnt;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public Long getProductCnt() {
        return productCnt;
    }
}
