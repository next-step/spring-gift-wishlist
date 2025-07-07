package gift.service;

import gift.auth.JwtTokenProvider;
import gift.dto.MemberInfoResponse;
import gift.dto.MemberRequest;
import gift.dto.MemberResponse;
import gift.entity.Member;
import gift.exception.LoginException;
import gift.repository.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse register(MemberRequest request) {
        Member member = new Member(request.email(), request.password());
        Member savedMember = memberRepository.save(member);

        String token = jwtTokenProvider.createToken(savedMember.getEmail());
        return new MemberResponse(token);
    }

    public MemberResponse login(MemberRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new LoginException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!member.getPassword().equals(request.password())) {
            throw new LoginException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(member.getEmail());
        return new MemberResponse(token);
    }

    public List<MemberInfoResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberInfoResponse::from)
                .toList();
    }

    public void saveMember(MemberRequest request) {
        Member member = new Member(request.email(), request.password());
        memberRepository.save(member);
    }
}
