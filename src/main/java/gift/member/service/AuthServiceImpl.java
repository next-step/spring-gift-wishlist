package gift.member.service;

import gift.member.JwtProvider;
import gift.member.dto.LoginRequestDto;
import gift.member.dto.LoginResponseDto;
import gift.member.entity.Member;
import gift.member.exception.LoginFailedException;
import gift.member.repository.MemberRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;

    public AuthServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        // DB 조회 -> 성공 시 Token 생성, 실패 시 로그인 실패

        Member foundMember;
        try {
            foundMember = memberRepository.findMemberByEmail(loginRequestDto.email());
        } catch (EmptyResultDataAccessException e) {
            throw new LoginFailedException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // 성공 시 password 확인 -> 성공 시 Token 생성, 실패 시 로그인 실패(403 Forbidden)
        if (!foundMember.getPassword().equals(loginRequestDto.password())) {
            // 실패 시 로그인 실패(403 Forbidden)
            throw new LoginFailedException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // 성공 시 Token 생성 후 반환
        String token = new JwtProvider().generateToken(foundMember.getMemberId(),
            foundMember.getName(),
            foundMember.getRole());

        return new LoginResponseDto(token);
    }
}
