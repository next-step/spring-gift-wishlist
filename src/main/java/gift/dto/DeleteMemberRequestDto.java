package gift.dto;

public record DeleteMemberRequestDto(
        String email,
        String password
) {
}
