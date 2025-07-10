package gift.dto;

public record MemberUpdateRequestDto(
        String name,
        String email,
        String password
) {}
