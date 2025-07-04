package gift.member.service;

import gift.member.dto.LoginRequestDto;
import gift.member.dto.LoginResponseDto;
import gift.member.dto.RegisterRequestDto;
import gift.member.dto.RegisterResponseDto;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public RegisterResponseDto registerMember(RegisterRequestDto registerRequestDto) {
        Member member = new Member(registerRequestDto.email(), registerRequestDto.password());
        memberRepository.saveMember(member);
        return null;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        // DB 조회 -> 성공 시 Token 생성, 실패 시 로그인 실패
        memberRepository.findMemberByEmail(loginRequestDto.email());

        // TODO: 성공 시 Token 생성

        return null;
    }
}
