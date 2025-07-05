package gift.repository;


import gift.entity.Member;
import gift.dto.MemberRequestDto;
import java.util.List;
import java.util.Optional;

//DIP를 준수해보자 -> DIP : 한 클래스는 다른 클래스에 직접 의존하지 않는다. (인터페이스에 의존)
public interface MemberRepository {

    //MemberRepository : 회원에 관한 CRUD...
    //회원 가입 -> 회원을 저장
    void addMember(MemberRequestDto memberRequestDto);

    //회원 조회 -> 이메일과 비밀번호를 통해 회원 조회(로그인시 확인...)
    Optional<Member> findMemberByEmailAndPassword(String email, String password);

    //회원 조회 -> 중복 검증을 위함
    Optional<Member> findMemberByEmail(String email);

    //모든 회원을 조회
    List<Member> findAllMember();

    Optional<Member> findMemberById(Long id);

    void modifyMember(Long id, MemberRequestDto memberRequestDto);

    void removeMemberById(Long id);


}
