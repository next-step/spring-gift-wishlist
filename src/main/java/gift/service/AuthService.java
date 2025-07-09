package gift.service;

import gift.dto.AuthResponseDto;
import gift.dto.MemberRequestDto;
import gift.util.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponseDto register(MemberRequestDto memberRequestDto) {
        return new AuthResponseDto(
                jwtTokenProvider.makeJwtToken(
                        memberService.creatMember(memberRequestDto)
                ));
    }

    public AuthResponseDto login(MemberRequestDto memberRequestDto) {

        return new AuthResponseDto(
                jwtTokenProvider.makeJwtToken(
                        memberService.login(memberRequestDto)
                ));
    }
}
