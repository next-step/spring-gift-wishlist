package gift.service;

import gift.JwtUtil;
import gift.dto.AuthToken;
import gift.entity.Member;
import gift.dto.AuthRequest;
import gift.entity.Role;
import gift.exception.DuplicateEmailException;
import gift.exception.UnauthorizedException;
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
            throw new DuplicateEmailException("이미 가입된 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        Member savedMember = memberRepository.save(new Member(request.email(), encodedPassword, Role.USER));
        return new AuthToken(jwtUtil.generateAccessToken(savedMember));
    }

    @Override
    public AuthToken login(AuthRequest request) {
        Member member = memberRepository.findByEmail(request.email())
            .orElseThrow(() -> new UnauthorizedException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtUtil.generateAccessToken(member);
        return new AuthToken(accessToken);
    }
}