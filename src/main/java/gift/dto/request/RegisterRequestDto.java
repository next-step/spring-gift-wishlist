package gift.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(

    @NotBlank
    String userRole,

    @Email
    String email,

    @NotBlank
    String password
) {

}
