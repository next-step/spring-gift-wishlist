package gift.dto;

public record MemberLoginRequestDTO(
        String email,
        String password
) {}