package gift.member.service;

import gift.exception.InvalidLoginException;
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
                throw new IllegalArgumentException();
            }

            return tokenProvider.generateToken(MemberResponseDto.from(member));

        }catch(IllegalArgumentException e){
            throw new InvalidLoginException();
        }
    }

    public String registerAndLogin(RegisterRequestDto registerRequestDto) {
        MemberResponseDto memberResponseDto = memberService.register(registerRequestDto);

        return tokenProvider.generateToken(memberResponseDto);
    }
}
