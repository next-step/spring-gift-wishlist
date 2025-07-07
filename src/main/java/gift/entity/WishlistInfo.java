package gift.entity;

public class WishlistInfo {
    private Long userId;
    private Long productId;
    private Long productCnt;
    
    public WishlistInfo(Long userId, Long productId, Long productCnt) {
        this.userId = userId;
        this.productId = productId;
        this.productCnt = productCnt;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public Long getProductCnt() {
        return productCnt;
    }
}
