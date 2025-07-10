package gift.service;

import gift.auth.JwtTokenProvider;
import gift.dto.MemberInfoResponse;
import gift.dto.MemberRequest;
import gift.dto.TokenResponse;
import gift.entity.Member;
import gift.exception.EmailAlreadyExistsException;
import gift.exception.LoginException;
import gift.repository.MemberRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenResponse register(MemberRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = new Member(request.email(), encodedPassword);
        Member savedMember = memberRepository.save(member);

        String token = jwtTokenProvider.createToken(savedMember.getId().toString());
        return new TokenResponse(token);
    }

    public TokenResponse login(MemberRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new LoginException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new LoginException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(member.getId().toString());
        return new TokenResponse(token);
    }

    public Member authenticate(MemberRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new LoginException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new LoginException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return member;
    }

    public List<MemberInfoResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberInfoResponse::from)
                .toList();
    }

    public void saveMember(MemberRequest request) {
        memberRepository.findByEmail(request.email())
                .ifPresent(member -> {
                    throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
                });

        Member member = new Member(request.email(), request.password());
        memberRepository.save(member);
    }
}
