package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface MemberRepository {
    public int addMember(Member member);
    public Member findMemberByIdOrElseThrow(Long id);
    public Member findMemberByEmail(String email);
    public List<Member> findAllMembers();
    public int updateMemberById(Member member);
    public int deleteMemberById(Long id);
}
