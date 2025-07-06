package gift.member.service;

import gift.member.dto.AdminMemberGetResponseDto;
import gift.member.dto.AdminMemberUpdateRequestDto;
import gift.member.security.JwtProvider;
import gift.member.dto.RegisterRequestDto;
import gift.member.dto.TokenResponseDto;
import gift.member.entity.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public TokenResponseDto registerMember(RegisterRequestDto requestDto) {
        Member member = new Member(requestDto.email(), requestDto.password(),
            requestDto.name());
        memberRepository.saveMember(member);

        Member savedMember = memberRepository.findMemberByEmail(requestDto.email());
        System.out.println(savedMember);

        String token = new JwtProvider().generateToken(savedMember.getMemberId(),
            savedMember.getName(),
            savedMember.getRole());

        return new TokenResponseDto(token);
    }

    @Override
    public void findMemberByEmail(RegisterRequestDto requestDto) {

        try {
            Member foundMember = memberRepository.findMemberByEmail(requestDto.email());
        } catch (EmptyResultDataAccessException e) {
            throw new MemberNotFoundException("이메일이 존재하지 않습니다.");
        }
    }

    @Override
    public List<AdminMemberGetResponseDto> findAllMembers() {
        return memberRepository.findAllMembers();
    }

    @Override
    public AdminMemberGetResponseDto findMemberById(Long memberId) {
        Member member = memberRepository.findMemberById(memberId);

        return new AdminMemberGetResponseDto(member.getMemberId(), member.getEmail(),
            member.getPassword(), member.getName(), member.getRole());
    }

    @Override
    public void updateMemberById(Long memberId,
        AdminMemberUpdateRequestDto requestDto) {
        findMemberById(memberId);

        Member member = new Member(memberId,
            requestDto.email(), requestDto.password(),
            requestDto.name(), requestDto.role());

        memberRepository.updateMemberById(member);
    }

    @Override
    public void deleteMemberById(Long memberId) {
        findMemberById(memberId);

        memberRepository.deleteMemberById(memberId);
    }
}
