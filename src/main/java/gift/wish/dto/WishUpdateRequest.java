package gift.wish.dto;

import jakarta.validation.constraints.NotNull;

public record WishUpdateRequest (
        @NotNull
        Long wishId,
        @NotNull
        Integer quantity
) {
}
