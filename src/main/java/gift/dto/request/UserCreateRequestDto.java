package gift.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequestDto(

    @Email
    String email,

    @NotBlank
    String password
) {

}
