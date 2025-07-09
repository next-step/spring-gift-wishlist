package gift.service;

import gift.dto.LoginResponseDto;
import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.exception.member.EmailAlreadyExistsException;
import gift.exception.member.LoginFailedException;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import gift.util.PasswordUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(MemberRepository memberRepository, PasswordUtil passwordUtil, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordUtil = passwordUtil;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public LoginResponseDto saveMember(MemberRequestDto dto) {
        if (memberRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException();
        }

        String encodedPassword = passwordUtil.encode(dto.password());
        Member member = new Member(dto.email(), encodedPassword);
        Member savedMember = memberRepository.saveMember(member);

        String token = jwtUtil.generateToken(savedMember.getEmail(), savedMember.getId(), savedMember.getRole());
        return new LoginResponseDto(token);
    }

    @Override
    @Transactional
    public LoginResponseDto loginMember(MemberRequestDto dto) {
        Member member = memberRepository.findByEmail(dto.email())
                .orElseThrow(LoginFailedException::new);

        if (!passwordUtil.matches(dto.password(), member.getPassword())) {
            throw new LoginFailedException();
        }

        String token = jwtUtil.generateToken(member.getEmail(), member.getId(), member.getRole());
        return new LoginResponseDto(token);
    }
}
