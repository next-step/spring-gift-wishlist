package gift.member.service;

import gift.member.dto.request.LoginRequestDto;
import gift.member.dto.response.MemberResponseDto;
import gift.member.dto.request.RegisterRequestDto;
import gift.member.entity.Member;
import gift.member.token.TokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    public AuthService(MemberService memberService, TokenProvider tokenProvider) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    public String login(LoginRequestDto loginRequestDto) {
        try{
            Member member = memberService.getMemberByEmail(loginRequestDto.email());

            if(!member.getPassword().equals(loginRequestDto.password())) {
                throw new IllegalArgumentException("잘못된 비밀번호입니다.");
            }

            return tokenProvider.generateToken(MemberResponseDto.from(member));

        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    public String registerAndLogin(RegisterRequestDto registerRequestDto) {
        MemberResponseDto memberResponseDto = memberService.register(registerRequestDto);

        return tokenProvider.generateToken(memberResponseDto);
    }
}
