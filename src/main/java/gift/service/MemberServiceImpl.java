package gift.service;

import gift.dto.LoginResponseDto;
import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public MemberServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public LoginResponseDto saveMember(MemberRequestDto dto) {
        if (memberRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.password());
        Member member = new Member(dto.email(), encodedPassword);
        Member savedMember = memberRepository.saveMember(member);

        String token = jwtUtil.generateToken(savedMember.getEmail(), savedMember.getId(), savedMember.getRole());
        return new LoginResponseDto(token);
    }

    @Override
    @Transactional
    public LoginResponseDto loginMember(MemberRequestDto dto) {
        Member member = memberRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 이메일입니다."));

        if (!passwordEncoder.matches(dto.password(), member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 비밀번호입니다.");
        }

        String token = jwtUtil.generateToken(member.getEmail(), member.getId(), member.getRole());
        return new LoginResponseDto(token);
    }
}
