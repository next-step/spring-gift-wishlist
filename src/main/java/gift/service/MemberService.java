package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.entity.Member;
import gift.exception.EmailAlreadyExistsException;
import gift.exception.InvalidCredentialsException;
import gift.exception.ResourceNotFoundException;
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
            throw new EmailAlreadyExistsException("이미 등록된 이메일입니다.");
        }

        Member member = new Member(memberRequestDto);
        memberRepository.registerMember(member);

        return new MemberResponseDto(jwtTokenProvider.generateToken(member));
    }

    public MemberResponseDto loginMember(MemberRequestDto memberRequestDto) {
        Member member = memberRepository.findMemberByEmail(memberRequestDto.email())
                .orElseThrow(() -> new ResourceNotFoundException("등록된 사용자가 아닙니다."));

        if (!member.getPassword().equals(memberRequestDto.password())) {
            throw new InvalidCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return new MemberResponseDto(jwtTokenProvider.generateToken(member));
    }
}
