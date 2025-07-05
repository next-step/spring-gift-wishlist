package gift.service;

import gift.config.JwtProvider;
import gift.dto.request.MemberRequsetDto;
import gift.dto.response.TokenResponseDto;
import gift.entity.Member;
import gift.exception.ForbiddenException;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;


@Service
public class MemberService {

    private MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider =  jwtProvider;
    }


    public TokenResponseDto register(MemberRequsetDto dto) {

        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        Member member = new Member(null, dto.getEmail(), dto.getPassword(),"USER");
        memberRepository.save(member);

        Member saved = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new IllegalStateException("회원 저장 실패"));

        String token = jwtProvider.generateToken(saved);
        return new TokenResponseDto(token);

    }


    public TokenResponseDto login(MemberRequsetDto dto) {
        Member saved = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ForbiddenException("가입 되지 않은 회원 입니다."));

        if (!saved.getPassword().equals(dto.getPassword())) {
            throw new ForbiddenException("비밀번호가 올바르지 않습니다.");
        }

        String token = jwtProvider.generateToken(saved);
        return new TokenResponseDto(token);
    }
}
