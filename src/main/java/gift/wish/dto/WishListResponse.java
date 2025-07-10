package gift.wish.dto;

public record WishListResponse(
        Long wishId,
        Long product_id,
        String productName,
        String productImageUrl,
        Integer quantity
) {

}
