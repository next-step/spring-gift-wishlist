package gift.wishlist.dto;

import jakarta.validation.constraints.NotBlank;

public record WishlistAddDto(
    @NotBlank(message = "itemId는 필수입니다.")
    Long itemId
) {

}
