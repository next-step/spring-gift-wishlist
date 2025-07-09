package gift.dto;

import gift.entity.Member;
import gift.entity.Role;

public record MemberResponseDto(Long id, String email, String password, Role role) {
    public MemberResponseDto(Member member) {
        this(member.getId(), member.getEmail(), member.getPassword(), member.getRole());
    }
}
