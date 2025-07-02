package gift.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ApprovedProductCreateRequestDto(
    @NotNull(message = "Product Name must not be null.")
    @Size(max = 15, message = "Product Name must be less than or equal to 15 characters.")
    @Pattern(
        regexp = "^(?=.*카카오)[\\p{L}\\p{N} ()\\[\\]+\\-&/_]*$",
        message = "Product name contains letters, numbers, and the following special characters are allowed: ( ) [ ] + - & / _"
    )
    String name) {

}