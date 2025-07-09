package gift.service;

import gift.dto.*;
import gift.entity.Member;
import gift.entity.Role;
import gift.exception.EmailAlreadyExistsException;
import gift.exception.InvalidPasswordException;
import gift.exception.MemberNotFoundException;
import gift.repository.MemberRepository;
import gift.config.JwtProvider;
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
    public TokenResponseDto register(MemberRegisterRequestDto memberRegisterRequestDto) {
        String email = memberRegisterRequestDto.email();
        if (memberRepository.findMemberByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(email);
        }

        String rawPassword = memberRegisterRequestDto.password();
        Member member = new Member(
                null,
                memberRegisterRequestDto.name(),
                memberRegisterRequestDto.email(),
                rawPassword,
                Role.USER
        );
        Member saved = memberRepository.saveMember(member);

        String token = jwtProvider.generateToken(saved);
        return new TokenResponseDto(token);
    }

    @Override
    public TokenResponseDto login(MemberLoginRequestDto memberLoginRequestDto) {
        Member member = memberRepository.findMemberByEmail(memberLoginRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!member.isPasswordMatch(memberLoginRequestDto.password())) {
            throw new InvalidPasswordException();
        }

        String token = jwtProvider.generateToken(member);

        return new TokenResponseDto(token);
    }

    @Override
    public List<MemberResponseDto> findAllMembers() {
        return memberRepository.findAllMembers().stream()
                .map(member -> new MemberResponseDto(
                        member.getId(),
                        member.getName(),
                        member.getEmail(),
                        member.getRole().name()))
                .toList();
    }

    @Override
    public MemberResponseDto findMemberById(Long id) {
        Member member = memberRepository.findMemberById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
        return new MemberResponseDto(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole().name()
        );
    }

    @Override
    public void updateMember(Long id, MemberUpdateRequestDto memberUpdateRequestDto) {
        Member member = memberRepository.findMemberById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
        member.update(memberUpdateRequestDto.name(), memberUpdateRequestDto.email(), memberUpdateRequestDto.password());
        memberRepository.updateMember(member);
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.findMemberById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
        memberRepository.deleteMember(id);
    }
}
