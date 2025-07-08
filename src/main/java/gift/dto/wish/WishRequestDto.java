package gift.dto.wish;

import jakarta.validation.constraints.NotBlank;

public record WishRequestDto(
        @NotBlank
        Long productId,
        Integer quantity
) {

}
