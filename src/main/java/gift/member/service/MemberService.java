package gift.member.service;

import gift.auth.JwtUtil;
import gift.member.domain.Member;
import gift.member.domain.RoleType;
import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberTokenResponse;
import gift.member.dto.MemberRegisterRequest;
import gift.member.dto.MemberUpdateRequest;
import gift.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public MemberTokenResponse register(MemberRegisterRequest request) {
        return registerMember(request, RoleType.USER);
    }

    public MemberTokenResponse registerAdmin(MemberRegisterRequest request) {
        return registerMember(request, RoleType.ADMIN);
    }

    private MemberTokenResponse registerMember(MemberRegisterRequest request, RoleType roleType) {
        if(memberRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + request.email());
        }

        Member member = memberRepository.save(request.email(), request.password(), roleType);

        return new MemberTokenResponse(jwtUtil.generateToken(member));
    }

    public MemberTokenResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        checkPassword(member, request.password(), "비밀번호가 일치하지 않습니다.");

        return new MemberTokenResponse(jwtUtil.generateToken(member));
    }

    public void updatePassword(Member member, MemberUpdateRequest request) {
        checkPassword(member, request.password(), "현재 비밀번호가 일치하지 않습니다.");

        memberRepository.updatePassword(member.getId(), request.password());
    }

    public void deleteMember(Member member, String password) {
        checkPassword(member, password, "비밀번호가 일치하지 않습니다.");

        memberRepository.deleteById(member.getId());
    }

    private void checkPassword(Member member, String password, String msg) {
        if(member.getPassword().equals(password)) {
            throw new IllegalArgumentException(msg);
        }
    }
}
