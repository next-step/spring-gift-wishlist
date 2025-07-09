package gift.member.dto;

public record MemberLoginResponseDto(
        MemberDto memberDto,
        MemberTokenDto tokenInfo
) {

}
