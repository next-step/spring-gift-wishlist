package gift.dto.member;

public record LoginRequest(
    String email,
    String password
) {}
