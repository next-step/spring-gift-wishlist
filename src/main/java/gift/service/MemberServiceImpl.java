package gift.service;

import gift.dto.*;
import gift.exception.InvalidCredentialsException;
import gift.repository.MemberRepository;
import gift.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Override
    public MemberResponseDto register(MemberRequestDto memberRequestDto) {
        return memberRepository.createMember(memberRequestDto);
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        MemberAuthDto auth = memberRepository.findAuthByEmail(loginRequestDto.email());

        if (!loginRequestDto.password().equals(auth.password())) {
            throw new InvalidCredentialsException("잘못된 이메일 또는 비밀번호입니다.");
        }

        return jwtProvider.generateToken(
                auth.id(),
                auth.email(),
                auth.role()
        );
    }

    @Override
    public PageResult<MemberResponseDto> findAllMembers(PageRequestDto pageRequestDto) {
        return memberRepository.findAllMembers(pageRequestDto);
    }

    @Override
    public MemberResponseDto findMemberById(Long id) {
        return memberRepository.findMemberById(id);
    }

    @Override
    public MemberResponseDto updateMember(Long id, MemberUpdateDto memberUpdateDto) {
        return memberRepository.updateMember(id, memberUpdateDto);
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteMember(id);
    }
}
