package gift.dto;

import gift.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberRequestDTO(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotNull
        Role role
) {}
