package gift.dto.wish;

import jakarta.validation.constraints.NotBlank;

public record WishResponseDto(
        Long wishListId,
        String productName,
        String productImage,
        Integer quantity,
        Integer price
) {

}
