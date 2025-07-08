package gift.repository;

import gift.entity.Member;
import gift.entity.RoleType;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    List<Member> findAllMembers();
    Optional<Member> findMemberById(Long id);
    Optional<Member> findMemberByEmail(String email);
    Member saveMember(String email, String password, RoleType role);
    int updateMember(Long id, RoleType role);
    int deleteMember(Long id);
}
