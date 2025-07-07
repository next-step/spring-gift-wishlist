package gift.member.service;

import gift.common.security.PasswordEncoder;
import gift.member.Member;
import gift.member.dto.MemberCreateDto;
import gift.member.dto.MemberResponseDto;
import gift.member.exception.DuplicateEmailException;
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
}
