package gift.service;

import gift.dto.MemberLogInRequestDto;
import gift.dto.MemberLogInResponseDto;
import gift.dto.MemberResponseDto;
import gift.entity.Member;
import gift.exception.InvalidPasswordException;
import gift.exception.MemberEmailAlreadyExistsException;
import gift.repository.MemberRepository;
import gift.util.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberServiceImpl(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public MemberLogInResponseDto registerMember(MemberLogInRequestDto requestDto) {
        if (memberRepository.findMemberByEmail(requestDto.email()) != null) {
            throw new MemberEmailAlreadyExistsException();
        }

        Member member = new Member(
            null,
            requestDto.email(),
            requestDto.password());
        member = memberRepository.registerMember(member);

        return new MemberLogInResponseDto(jwtProvider.createToken(member));
    }

    @Override
    public MemberLogInResponseDto findMemberToLogIn(MemberLogInRequestDto requestDto) {
        Member member = memberRepository.findMemberByEmail(requestDto.email());

        if (!requestDto.password().equals(member.getPassword())) {
            throw new InvalidPasswordException();
        }

        return new MemberLogInResponseDto(jwtProvider.createToken(member));
    }

    @Override
    public List<MemberResponseDto> findAllMembers() {
        return memberRepository
            .findAllMembers()
            .stream()
            .map(MemberResponseDto::from)
            .toList();
    }

    @Override
    public MemberResponseDto findMemberById(Long id) {
        return MemberResponseDto.from(memberRepository.findMemberById(id));
    }

    @Override
    public void updateMember(Long id, MemberLogInRequestDto requestDto) {
        Member member = new Member(
            id,
            requestDto.email(),
            requestDto.password()
        );
        memberRepository.updateMember(member);
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteMember(id);
    }
}
