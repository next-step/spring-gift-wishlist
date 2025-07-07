package gift.member.service.impl;

import gift.jwt.JwtService;
import gift.member.dto.LoginRequestDto;
import gift.member.dto.LoginResponseDto;
import gift.member.dto.SignUpRequestDto;
import gift.member.dto.SignUpResponseDto;
import gift.member.exception.AuthenticationException;
import gift.member.exception.DuplicatedException;
import gift.member.model.Member;
import gift.member.repository.MemberRepository;
import gift.member.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    public MemberServiceImpl(MemberRepository memberRepository, JwtService jwtService) {
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
    }

    @Override
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        if (memberRepository.existsByEmail(signUpRequestDto.email())) {
            throw new DuplicatedException("이미 사용 중인 이메일입니다.");
        }

        Member member = new Member(signUpRequestDto.email(), signUpRequestDto.password());
        Member savedMember = memberRepository.save(member);

        // JWT 토큰 생성
        String accessToken = jwtService.createToken(
                savedMember.getId(),
                savedMember.getEmail(),
                savedMember.getRole());

        return new SignUpResponseDto(accessToken);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new AuthenticationException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!loginRequestDto.password().equals(member.getPassword())) {
            throw new AuthenticationException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        String accessToken = jwtService.createToken(
                member.getId(),
                member.getEmail(),
                member.getRole()
        );

        return new LoginResponseDto(accessToken);
    }
}
