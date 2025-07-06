package gift.dto.itemDto.userDto;

import jakarta.validation.constraints.NotNull;

public record UserUpdateDto (
        @NotNull String email,
        @NotNull String password
) {
}
