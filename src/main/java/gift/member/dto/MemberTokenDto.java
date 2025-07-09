package gift.member.dto;

public record MemberTokenDto(
        String accessToken,
        String refreshToken
) {

}
