package gift.dto;

import gift.entity.Member;

public record MemberResponseDto(
    Long id,
    String email,
    String password
) {
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(
            member.getId(),
            member.getEmail(),
            member.getPassword()
        );
    }
}
