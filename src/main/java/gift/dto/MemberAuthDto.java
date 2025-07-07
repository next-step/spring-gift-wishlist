package gift.dto;

public record MemberAuthDto(
        Long   id,
        String email,
        String password,
        String role
) { }