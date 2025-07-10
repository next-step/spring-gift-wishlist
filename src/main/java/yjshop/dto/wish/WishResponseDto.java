package yjshop.dto.wish;

public record WishResponseDto(
        Long wishListId,
        String productName,
        String productImage,
        Integer quantity,
        Integer price
) {

}
