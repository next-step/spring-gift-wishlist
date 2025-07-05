package gift.dto;

import jakarta.validation.constraints.Email;

public record MemberRequestDto(
        @Email
        String email,
        String password
) {
}
