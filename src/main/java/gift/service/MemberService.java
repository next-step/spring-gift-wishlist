package gift.service;

import gift.entity.Member;
import gift.dto.MemberRequestDto;
import gift.repository.MemberRepository;
import gift.repository.MemberRepositoryImpl;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepositoryImpl memberRepository){
        this.memberRepository = memberRepository;
    }

    //TODO:멤버 회원 가입 -> 리포지토리에 저장
    public boolean register(MemberRequestDto memberRequestDto){
        //중복을 확인하고
        if(checkDuplicateEmail(memberRequestDto.email())){
            return false;
        }
        memberRepository.addMember(memberRequestDto);
        return true;
    }

    //TODO:로그인 기능 -> 이메일과 비밀번호가 일치하는지 확인하는 로직
    public Boolean checkMember(MemberRequestDto memberRequestDto){
        Optional<Member> member = memberRepository.findMemberByEmailAndPassword(memberRequestDto.email(), memberRequestDto.password());
        if(member.isEmpty()){
            return false;
        }
        return true;
    }

    //TODO:중복 이메일 불가 -> 회원 가입시, 해당 이메일로 가입된 정보가 있는지 확인하기
    boolean checkDuplicateEmail(String email){
        Optional<Member> member = memberRepository.findMemberByEmail(email);
        if(member.isPresent()){
            return true;
        }
        return false;
    }

    //특정 멤버를 조회하는 기능
    public Optional<Member> findMember(Long id){
        return memberRepository.findMemberById(id);
    }

    public Optional<Member> getMemberByEmail(String email){
        return memberRepository.findMemberByEmail(email);
    }



    //멤버의 정보를 수정하는 기능
    public void modifyMember(Long id, MemberRequestDto memberRequestDto){
        memberRepository.modifyMember(id, memberRequestDto);
    }

    //멤버를 삭제하는 기능
    public void removeMember(Long id){
        memberRepository.removeMemberById(id);
    }

    public List<Member> getAllMembers(){
        return memberRepository.findAllMember();
    }

}
