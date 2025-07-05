package gift.dto.itemDto.userDto;

import jakarta.validation.constraints.NotNull;

public record UserResponseDto(
        @NotNull String email,
        @NotNull String password
) {
}
