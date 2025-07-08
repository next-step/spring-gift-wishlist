package gift.repository;

import gift.domain.Member;
import gift.domain.Role;
import gift.dto.MemberInfoResponseDto;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {

  MemberInfoResponseDto saveMember(Member member);

  List<MemberInfoResponseDto> searchAllMembers();

  Optional<Member> searchMemberById(Long id);

  Optional<Member> searchMemberByEmail(String email);

  Member updateMember(Long id, String email, String password, Role role);

  void deleteMember(Long id);
}
