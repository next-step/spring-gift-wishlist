package gift.dto.wish;

import jakarta.validation.constraints.NotBlank;

public record WishResponseDto(
        String productName,
        String productImage,
        Integer quantity,
        Integer price
) {

}
