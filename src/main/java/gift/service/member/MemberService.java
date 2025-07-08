package gift.service.member;

import gift.domain.Member;
import gift.dto.jwt.TokenResponse;
import gift.dto.member.LoginRequest;
import gift.dto.member.MemberRequest;
import gift.dto.member.MemberResponse;
import gift.global.exception.ErrorCode;
import gift.global.exception.InvalidMemberException;
import gift.global.jwt.JwtUtil;
import gift.repository.member.MemberRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public TokenResponse login(LoginRequest request) {
        Member member;
        try {
            member = memberRepository.findByEmail(request.email());
        } catch (DataAccessException e) {
            // member를 찾지 못한 경우: 해당 이메일로 가입한 계정이 없는 경우
            throw new InvalidMemberException(ErrorCode.INCORRECT_LOGIN_INFO,
                ErrorCode.INCORRECT_LOGIN_INFO.getErrorMessage());
        }

        if(member==null || !member.getPassword().equals(request.password())){
            throw new InvalidMemberException(ErrorCode.INCORRECT_LOGIN_INFO);
        }

        String token = jwtUtil.generateToken(member.getId());
        return new TokenResponse(token);
    }

    public Long insert(MemberRequest request){

        return memberRepository.insert(new Member(null, request.email(), request.password()));
    }

    public MemberResponse findById(Long memberId) {
        Member member = memberRepository.findById(memberId);
        return new MemberResponse(member.getEmail(), member.getPassword());
    }

    public void update(Long memberId, MemberRequest request){
        memberRepository.findById(memberId);

        memberRepository.update(memberId, request);
    }

    public void deleteById(Long memberId) {
        memberRepository.findById(memberId);

        memberRepository.deleteById(memberId);
    }
}
