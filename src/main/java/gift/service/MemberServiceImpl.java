package gift.service;

import gift.JwtUtil;
import gift.dto.AuthToken;
import gift.entity.Member;
import gift.dto.AuthRequest;
import gift.entity.Role;
import gift.exception.DuplicateEmailException;
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
}
