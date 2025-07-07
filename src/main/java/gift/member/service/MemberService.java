package gift.member.service;

import gift.auth.JwtUtil;
import gift.member.domain.Member;
import gift.member.domain.RoleType;
import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberLoginResponse;
import gift.member.dto.MemberRegisterRequest;
import gift.member.repository.MemberRespository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRespository memberRespository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRespository memberRespository, JwtUtil jwtUtil) {
        this.memberRespository = memberRespository;
        this.jwtUtil = jwtUtil;
    }

    public Member register(MemberRegisterRequest request) {
        if(memberRespository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + request.email());
        }

        return memberRespository.save(request.email(), request.password(), RoleType.USER);
    }

    public MemberLoginResponse login(MemberLoginRequest request) {
        Member member = memberRespository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!member.getPassword().equals(request.password())){
           throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(member);

        return new MemberLoginResponse(token);
    }
}
