package gift.member.service;

import gift.common.security.PasswordEncoder;
import gift.member.Member;
import gift.member.dto.LoginRequestDto;
import gift.member.dto.MemberCreateDto;
import gift.member.dto.MemberResponseDto;
import gift.member.exception.DuplicateEmailException;
import gift.member.exception.InvalidLoginException;
import gift.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public MemberResponseDto create(MemberCreateDto memberCreateDto) {
        // 이메일 중복 확인
        memberRepository.findByEmail(memberCreateDto.email()).ifPresent(member -> {
            throw new DuplicateEmailException(memberCreateDto.email());
        });

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(memberCreateDto.password());

        Member member = new Member(
            memberCreateDto.name(),
            memberCreateDto.email(),
            encodedPassword
        );

        // 도메인 객체 저장
        Member savedMember = memberRepository.save(member);

        return new MemberResponseDto(
            savedMember.getId(),
            savedMember.getName(),
            savedMember.getEmail()
        );
    }

    public MemberResponseDto login(LoginRequestDto loginRequestDto) {
        // 요청 받은 이메일에 해당하는 비밀번호와 요청받은 비밀번호 비교
        Member member = memberRepository.findByEmail(loginRequestDto.email())
            .filter(m -> m.getPassword().equals(
                passwordEncoder.encode(loginRequestDto.password())
            ))
            .orElseThrow(InvalidLoginException::new);

        return new MemberResponseDto(
            member.getId(),
            member.getName(),
            member.getEmail()
        );
    }
}
