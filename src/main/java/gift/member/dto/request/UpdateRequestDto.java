package gift.member.dto.request;

public record UpdateRequestDto(
        String email,
        String password,
        String role
) {
}
