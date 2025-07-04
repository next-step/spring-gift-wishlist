package gift.repository;

//DIP를 준수해보자 -> DIP : 한 클래스는 다른 클래스에 직접 의존하지 않는다. (인터페이스에 의존)
public interface MemberRepository {

    //MemberRepository : 회원에 관한 CRUD...
    //회원 가입 -> 회원을 저장
    void addMember();

    //회원 조회 -> 이메일과 비밀번호를 통해 회원 조회(로그인시 확인...)
    void findMemberByEmailAndPassword();

    //회원 조회 -> 중복 검증을 위함
    void findMemberById();

}
