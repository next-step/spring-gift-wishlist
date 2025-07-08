package gift.service;

import gift.dto.MemberLogInRequestDto;
import gift.dto.MemberLogInResponseDto;
import gift.dto.MemberResponseDto;
import gift.entity.Member;
import gift.exception.InvalidPasswordException;
import gift.exception.MemberEmailAlreadyExistsException;
import gift.exception.MemberEmailNotExistsException;
import gift.repository.MemberRepository;
import gift.util.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if (memberRepository.findMemberByEmail(requestDto.email()).isPresent()) {
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
        Optional<Member> memberOptional = memberRepository.findMemberByEmail(requestDto.email());
        Member member = memberOptional.orElseThrow(MemberEmailNotExistsException::new);

        if (!requestDto.password().equals(member.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
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
    public MemberResponseDto updateMember(Long id, MemberLogInRequestDto requestDto) {
        Member member = new Member(
            id,
            requestDto.email(),
            requestDto.password()
        );
        memberRepository.updateMember(member);

        return MemberResponseDto.from(member);
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteMember(id);
    }
}
