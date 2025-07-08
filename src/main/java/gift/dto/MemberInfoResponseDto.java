package gift.dto;

import gift.domain.Member;
import gift.domain.Role;

public record MemberInfoResponseDto(Long id, String email, String password, Role role) {

  public MemberInfoResponseDto(Member member) {
    this(
        member.getId(),
        member.getEmail(),
        member.getPassword(),
        member.getRole()
    );
  }
}
