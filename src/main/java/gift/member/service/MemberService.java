package gift.member.service;

import gift.auth.JwtUtil;
import gift.member.domain.Member;
import gift.member.domain.RoleType;
import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberTokenResponse;
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

    public MemberTokenResponse register(MemberRegisterRequest request) {
        if(memberRespository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + request.email());
        }

        Member member = memberRespository.save(request.email(), request.password(), RoleType.USER);

        return new MemberTokenResponse(jwtUtil.generateToken(member));
    }

    public MemberTokenResponse login(MemberLoginRequest request) {
        Member member = memberRespository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!member.getPassword().equals(request.password())){
           throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return new MemberTokenResponse(jwtUtil.generateToken(member));
    }
}
