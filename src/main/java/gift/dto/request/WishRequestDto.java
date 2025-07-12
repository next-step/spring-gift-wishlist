package gift.dto.request;

import jakarta.validation.constraints.NotBlank;

public record WishRequestDto(

    @NotBlank
    String productName
) {

}
