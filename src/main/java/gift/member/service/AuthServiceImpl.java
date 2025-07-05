package gift.member.service;

import gift.member.dto.LoginRequestDto;
import gift.member.dto.LoginResponseDto;
import gift.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;

    public AuthServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        // TODO: DB 조회 -> 성공 시 Token 생성, 실패 시 로그인 실패(상태코드 처리 어떻게 하지)
        memberRepository.findMemberByEmail(loginRequestDto.email());

        // TODO: 성공 시 pass word 확인 -> 성공 시 Token 생성, 실패 시 로그인 실패(403 Forbidden)

        return null;
    }
}
