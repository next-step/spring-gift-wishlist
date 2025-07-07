package gift.member.dto;

public record MemberTokenInfo(
        String accessToken,
        String refreshToken
) {

}
