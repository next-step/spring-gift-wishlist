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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil,
        PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
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

        // Member 클래스에 정의된 비밀번호 확인 메서드를 사용하도록 변경
        if (member == null || !member.matches(request.password(), passwordEncoder)) {
            throw new InvalidMemberException(ErrorCode.INCORRECT_LOGIN_INFO,
                ErrorCode.INCORRECT_LOGIN_INFO.getErrorMessage());
        }

        String token = jwtUtil.generateToken(member.getId());
        return new TokenResponse(token);
    }

    public Long insert(MemberRequest request) {
        return memberRepository.insert(
            Member.of(request.email(), request.password(), passwordEncoder));
    }

    public MemberResponse findById(Long memberId) {
        Member member = memberRepository.findById(memberId);
        return new MemberResponse(member.getEmail(), member.getPassword());
    }

    public void update(Long memberId, MemberRequest request) {
        // member 필드값의 수정은 Member 클래스에서 담당하는 게 책임분리 원칙에 더 알맞다고 생각했습니다.
        // member 클래스의 update 메서드를 호출해서 인코딩된 비밀번호 등으로 필드값을 수정합니다.
        Member member = memberRepository.findById(memberId);
        member.update(request, passwordEncoder);

        memberRepository.update(member);
    }

    public void deleteById(Long memberId) {
        memberRepository.findById(memberId);

        memberRepository.deleteById(memberId);
    }
}
