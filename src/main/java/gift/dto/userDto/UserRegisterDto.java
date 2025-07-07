package gift.dto.userDto;

import jakarta.validation.constraints.NotNull;

public record UserRegisterDto(
        @NotNull String email,
        @NotNull String password
) {
}
