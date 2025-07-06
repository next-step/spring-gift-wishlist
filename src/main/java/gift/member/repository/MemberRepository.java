package gift.member.repository;

import gift.member.dto.AdminMemberGetResponseDto;
import gift.member.entity.Member;
import java.util.List;

public interface MemberRepository {

    void saveMember(Member member);

    Member findMemberByEmail(String email);

    List<AdminMemberGetResponseDto> findAllMembers();

    Member findMemberById(Long memberId);

    void updateMemberById(Member member);

    void deleteMemberById(Long memberId);
}
