package gift.service;

import gift.dto.LoginRequestDTO;
import gift.dto.RegisterRequestDTO;
import gift.dto.TokenResponseDTO;
import gift.entity.Member;
import gift.exception.DuplicateEmailException;
import gift.exception.InvalidCredentialsException;
import gift.exception.InvalidCredentialsException.ErrorType;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public TokenResponseDTO register(RegisterRequestDTO req) {
        if (memberRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateEmailException("사용중인 이메일입니다.");
        }

        Member member = new Member();
        member.setEmail(req.getEmail());
        member.setPassword(req.getPassword());

        memberRepository.save(member);
        return new TokenResponseDTO("token");
    }
    public TokenResponseDTO login(LoginRequestDTO req) {
        Member member = memberRepository.findByEmail(req.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("가입되지 않은 이메일입니다.", ErrorType.EMAIL_NOT_FOUND));

        if (!req.getPassword().equals(member.getPassword())) {
            throw new InvalidCredentialsException("비밀번호가 일치하지 않습니다.", ErrorType.PASSWORD_MISMATCH);
        }
        return new TokenResponseDTO("token");
    }
}
