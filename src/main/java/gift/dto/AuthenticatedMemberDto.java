package gift.dto;

public record AuthenticatedMemberDto(
        Long id,
        String email,
        String role
) {}
