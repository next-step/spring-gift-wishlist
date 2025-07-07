package gift.dto;

public record MemberUpdateDto(
        String email,
        String password,
        String role
) { }