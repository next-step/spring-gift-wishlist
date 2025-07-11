package gift.dto;

public class WishListRequestDto {

    private Long productId;
    private Integer quantity;

    public WishListRequestDto(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }
    public Integer getQuantity() {
        return quantity;
    }
}
