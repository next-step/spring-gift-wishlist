package giftproject.member.service;

import giftproject.member.dto.MemberRequestDto;
import giftproject.member.entity.Member;
import giftproject.member.repository.MemberRepository;
import giftproject.member.util.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String register(MemberRequestDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.password());

        Member newMember = new Member(requestDto.email(), encodedPassword);
        Member savedMember = memberRepository.save(newMember);

        return jwtTokenProvider.generateToken(savedMember.getId(), savedMember.getEmail());
    }

    public String login(MemberRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(requestDto.password(), member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.generateToken(member.getId(), member.getEmail());
    }
}
