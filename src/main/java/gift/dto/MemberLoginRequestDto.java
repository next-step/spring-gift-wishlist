package gift.dto;

public record MemberLoginRequestDto(
        String email,
        String password
) {}