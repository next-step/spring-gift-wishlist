package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WishListRequest(
        @NotNull
        Long productId,

        @NotNull
        @Min(1)
        Integer quantity) {

}
