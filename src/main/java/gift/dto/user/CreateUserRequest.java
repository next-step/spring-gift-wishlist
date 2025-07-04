package gift.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(

        @Email
        String email,

        @NotBlank
        String password) {
}
