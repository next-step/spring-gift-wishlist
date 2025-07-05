package gift.member.service;

import gift.member.security.JwtProvider;
import gift.member.dto.RegisterRequestDto;
import gift.member.dto.TokenResponseDto;
import gift.member.entity.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public TokenResponseDto registerMember(RegisterRequestDto registerRequestDto) {
        Member member = new Member(registerRequestDto.email(), registerRequestDto.password(),
            registerRequestDto.name());
        memberRepository.saveMember(member);

        Member savedMember = memberRepository.findMemberByEmail(registerRequestDto.email());
        System.out.println(savedMember);

        String token = new JwtProvider().generateToken(savedMember.getMemberId(),
            savedMember.getName(),
            savedMember.getRole());

        return new TokenResponseDto(token);
    }

    @Override
    public void findMemberByEmail(RegisterRequestDto registerRequestDto) {

        try {
            Member foundMember = memberRepository.findMemberByEmail(registerRequestDto.email());
        } catch (EmptyResultDataAccessException e) {
            throw new MemberNotFoundException("이메일이 존재하지 않습니다.");
        }
    }
}
