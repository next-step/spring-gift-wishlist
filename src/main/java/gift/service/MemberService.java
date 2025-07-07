package gift.service;

import gift.dto.LoginResponse;
import gift.dto.MemberRegisterRequest;
import gift.dto.MemberLoginRequest;
import gift.entity.Member;
import gift.entity.Role;
import gift.exception.DuplicateItemException;
import gift.exception.LoginException;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse register(MemberRegisterRequest request) {
        memberRepository.findByEmail(request.email()).ifPresent(member -> {
            throw new DuplicateItemException("이미 가입된 이메일입니다.");
        });

        Member newMember = new Member(null, request.email(), request.password(), Role.USER);
        memberRepository.save(newMember);

        String token = jwtUtil.createToken(newMember.getEmail(), newMember.getRole().name());
        return new LoginResponse(token);
    }

    public LoginResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
            .orElseThrow(() -> new LoginException("가입되지 않은 이메일입니다."));

        if (!member.getPassword().equals(request.password())) {
            throw new LoginException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(member.getEmail(), member.getRole().name());
        return new LoginResponse(token);
    }
}