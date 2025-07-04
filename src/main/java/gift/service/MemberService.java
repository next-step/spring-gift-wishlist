package gift.service;

import gift.dto.Member;
import gift.dto.MemberRequestDto;
import gift.repository.MemberRepository;
import gift.repository.MemberRepositoryImpl;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepositoryImpl memberRepository){
        this.memberRepository = memberRepository;
    }

    //TODO:멤버 회원 가입 -> 리포지토리에 저장
    public String register(MemberRequestDto memberRequestDto){
        //중복을 확인하고
        if(!checkUniqueEmail(memberRequestDto.email())){

        }
        Member member = new Member();
        memberRepository.addMember(member);
        //JWT에서 토큰 생성 후, 반환;
        String tempToken = "mytoken";
        return tempToken;
    }

    //TODO:로그인 기능 -> 이메일과 비밀번호가 일치하는지 확인하는 로직
    public Boolean checkMember(MemberRequestDto memberRequestDto){
        String Encryptedpw = "password";
        Optional<Member> member = memberRepository.findMemberByEmailAndPassword(memberRequestDto.email(), Encryptedpw);
        if(member.isEmpty()){
            return false;
        }
        return true;
    }

    //TODO:중복 이메일 불가 -> 회원 가입시, 해당 이메일로 가입된 정보가 있는지 확인하기
    boolean checkUniqueEmail(String email){
        Optional<Member> member = memberRepository.findMemberByEmail(email);
        if(member.isEmpty()){
            return false;
        }
        return true;
    }

}
