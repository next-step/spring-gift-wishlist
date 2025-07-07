package gift.service;

import gift.dto.AuthLogInRequestDto;
import gift.dto.AuthResponseDto;
import gift.dto.AuthSignUpRequestDto;
import gift.entity.Member;
import gift.exception.InvalidPasswordException;
import gift.exception.MemberEmailAlreadyExistsException;
import gift.exception.MemberEmailNotExistsException;
import gift.repository.MemberRepository;
import gift.util.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public AuthResponseDto registerMember(AuthSignUpRequestDto requestDto) {
        if (memberRepository.findMemberByEmail(requestDto.email()).isPresent()) {
            throw new MemberEmailAlreadyExistsException();
        }

        Member member = new Member(
            null,
            requestDto.email(),
            requestDto.password());
        member = memberRepository.registerMember(member);

        return new AuthResponseDto(jwtProvider.createToken(member));
    }

    @Override
    public AuthResponseDto findMemberToLogIn(AuthLogInRequestDto requestDto) {
        Optional<Member> memberOptional = memberRepository.findMemberByEmail(requestDto.email());
        Member member = memberOptional.orElseThrow(MemberEmailNotExistsException::new);

        if (!requestDto.password().equals(member.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        return new AuthResponseDto(jwtProvider.createToken(member));
    }
}
