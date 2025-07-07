package gift.service.member;

import gift.domain.Member;
import gift.dto.jwt.TokenResponse;
import gift.dto.member.LoginRequest;
import gift.dto.member.MemberRequest;
import gift.dto.member.MemberResponse;
import gift.global.jwt.JwtUtil;
import gift.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public TokenResponse login(LoginRequest request){
        Member member = memberRepository.findByEmail(request.email());

        if(member==null || !member.getPassword().equals(request.password())){
            throw new RuntimeException("이메일, 비밀번호 조합이 잘못되었습니다.");
        }

        String token = jwtUtil.generateToken(member.getEmail());
        return new TokenResponse(token);
    }

    public Long insert(MemberRequest request){

        return memberRepository.insert(new Member(null, request.email(), request.password()));
    }

    public MemberResponse findById(Long memberId){
        Member member = memberRepository.findById(memberId);
        return new MemberResponse(member.getEmail(), member.getPassword());
    }

    public void update(Long memberId, MemberRequest request){
        memberRepository.findById(memberId);

        memberRepository.update(memberId, request);
    }

    public void deleteById(Long memberId){
        memberRepository.findById(memberId);

        memberRepository.deleteById(memberId);
    }
}
