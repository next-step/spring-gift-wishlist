package gift.service;

import gift.config.JwtProvider;
import gift.dto.request.MemberRequsetDto;
import gift.dto.response.TokenResponseDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.jwtProvider = new JwtProvider();
    }


    public TokenResponseDto register(MemberRequsetDto dto) {
        Member member = new Member(null, dto.getEmail(), dto.getPassword());
        memberRepository.save(member);

        Member saved = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new IllegalStateException("회원 저장 실패"));

        String token = jwtProvider.generateToken(saved);
        return new TokenResponseDto(token);

    }
}
