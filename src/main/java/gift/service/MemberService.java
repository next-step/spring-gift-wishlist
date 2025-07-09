package gift.service;

import gift.JwtTokenProvider;
import gift.dto.MemberDto;
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

    public MemberDto register(MemberDto dto) {

        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        Member member = Member.createMember(dto.getEmail(), dto.getPassword());
        Member saved = memberRepository.save(member);

        return new MemberDto(saved);
    }

    public String login(String email, String password) throws IllegalAccessException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));


        member.validatePassword(password);

        return jwtTokenProvider.createToken(member.getEmail());
    }

}
