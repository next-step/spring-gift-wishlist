package gift.dto;

import jakarta.validation.constraints.Email;

public record MemberLoginRequestDto(
        @Email
        String email,
        String password) {
}
