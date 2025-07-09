// gift/service/member/MemberService.java
package gift.service.member;

import gift.dto.member.AuthRequest;
import gift.dto.member.AuthResponse;
import gift.entity.member.Member;
import java.util.List;
import java.util.Optional;

public interface MemberService {

    AuthResponse register(AuthRequest req);

    AuthResponse login(String email, String rawPassword);

    List<Member> getAllMembers(String role);

    Optional<Member> getMemberById(Long id, String role);

    Member createMember(String email, String rawPassword, String newRole, String role);

    Member updateMember(Long id, String email, String rawPassword, String newRole, String role);

    void deleteMember(Long id, String role);
}
