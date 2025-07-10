package gift.dto;

public record MemberRegisterRequestDto(
        String name,
        String email,
        String password
) {}
