package gift.dto;

public record MemberRequestDto(
        String name,
        String password,
        String email,
        String role
) { }
