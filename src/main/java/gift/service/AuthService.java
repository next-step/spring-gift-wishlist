package gift.service;

import gift.domain.Member;
import gift.dto.MemberRegisterRequest;
import gift.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public AuthService(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    public String registerAndIssueToken(MemberRegisterRequest request) {
        Member member = memberService.register(request);
        return jwtUtil.createToken(member);
    }
}

