package gift.service;

import gift.auth.JwtTokenProvider;
import gift.dto.MemberRequest;
import gift.dto.MemberResponse;
import gift.entity.Member;
import gift.repository.MemberRepository;
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
}