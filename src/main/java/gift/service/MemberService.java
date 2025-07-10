package gift.service;

import gift.auth.JwtProvider;
import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.exception.forbidden.EmailDuplicateException;
import gift.exception.forbidden.EmailNotFoundException;
import gift.exception.forbidden.WrongPasswordException;
import gift.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public void register(MemberRequestDto dto) {
        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailDuplicateException();
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member member = new Member(null, dto.getEmail(), encodedPassword, "USER");
        memberRepository.save(member);
    }

    public String login(MemberRequestDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(EmailNotFoundException::new);

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new WrongPasswordException();
        }

        return jwtProvider.createToken(member);
    }
}
