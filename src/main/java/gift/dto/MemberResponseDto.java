package gift.dto;

import gift.entity.Member;
import gift.entity.Product;

public record MemberResponseDto (
        Long id,
        String email,
        String name,
        String role
) {
    public MemberResponseDto(Member member) {
        this(member.id(), member.email(), member.name(), member.role());
    }
}
