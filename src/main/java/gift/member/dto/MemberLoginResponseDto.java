package gift.member.dto;

public record MemberLoginResponseDto(
        MemberInfo memberInfo,
        MemberTokenInfo tokenInfo
) {

}
