package gift.service;

import gift.exception.DuplicatedEmailException;
import gift.exception.LoginFailedException;
import gift.exception.UnAuthenticatedException;
import gift.util.JwtUtil;
import gift.dto.AuthToken;
import gift.entity.Member;
import gift.dto.AuthRequest;
import gift.entity.Role;
import gift.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public MemberServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthToken register(AuthRequest request) {
        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicatedEmailException("사용할 수 없는 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        Member savedMember = memberRepository.save(new Member(request.email(), encodedPassword, Role.USER));
        return new AuthToken(jwtUtil.generateAccessToken(savedMember));
    }

    @Override
    public AuthToken login(AuthRequest request) {
        Member member = memberRepository.findByEmail(request.email())
            .orElse(null);

        if (member == null || !passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new LoginFailedException("이메일 또는 비밀번호가 틀렸습니다.");
        }
        String accessToken = jwtUtil.generateAccessToken(member);
        return new AuthToken(accessToken);
    }
}