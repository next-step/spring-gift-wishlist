package gift.dto;

public class WishListResponseDto {

    private Long id;

    private Long productId;

    private Integer quantity;

    public WishListResponseDto(Long id, Long productId, Integer quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

}
