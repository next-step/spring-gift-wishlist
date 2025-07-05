package gift.dto.itemDto.userDto;

import jakarta.validation.constraints.NotNull;

public record UserRegisterDto(
        @NotNull String email,
        @NotNull String password
) {
}
