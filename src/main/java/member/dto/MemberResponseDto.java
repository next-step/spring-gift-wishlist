package member.dto;

import member.entity.Member;

public record MemberResponseDto(
        Long id,
        String email
) {

    public MemberResponseDto(Member member) {
        this(member.getId(), member.getEmail());
    }

    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(member.getId(), member.getEmail());
    }
}
