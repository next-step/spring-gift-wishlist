package gift.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserAuthRequestDto(

    @Email
    String email,

    @NotBlank
    String password
) {

}
