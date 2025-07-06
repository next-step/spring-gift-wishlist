package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.security.JwtTokenProvider;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponseDto registerMember(MemberRequestDto memberRequestDto) {
        Optional<Member> optionalMember = memberRepository.findMemberByEmail(memberRequestDto.email());

        if (optionalMember.isPresent()) {
            throw new IllegalStateException("이미 등록된 이메일입니다.");
        }

        Member member = new Member(memberRequestDto);
        memberRepository.registerMember(member);

        return new MemberResponseDto(jwtTokenProvider.generateToken(member));
    }

    public MemberResponseDto loginMember(MemberRequestDto memberRequestDto) {
        Optional<Member> optionalMember = memberRepository.findMemberByEmail(memberRequestDto.email());

        if (optionalMember.isEmpty()) {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }

        if (optionalMember.get().getPassword().equals(memberRequestDto.password())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        return new MemberResponseDto(jwtTokenProvider.generateToken(optionalMember.get()));
    }
}
