package gift.entity;

import jakarta.validation.constraints.NotNull;

public record User(
        @NotNull String email,
        @NotNull String password
) {
}
