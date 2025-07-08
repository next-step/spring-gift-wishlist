package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.entity.Member;
import gift.exception.DuplicateEmailException;
import gift.exception.LoginFailedException;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthMemberService implements MemberService {

    private MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthMemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public MemberResponseDto signUp(MemberRequestDto requestDto) {
        // 이메일 중복 체크
        if (memberRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new DuplicateEmailException(requestDto.email());
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.password());

        // 회원 저장
        Member member = new Member(requestDto.email(), encodedPassword, requestDto.role());
        memberRepository.saveMember(member);

        // 토큰 생성
        String token = jwtUtil.generateToken(member.getEmail(), member.getRole().name());
        return new MemberResponseDto(token);
    }

    @Override
    public MemberResponseDto login(MemberRequestDto requestDto) {
        // 이메일 확인
        Member member = memberRepository.findByEmail(requestDto.email())
                .orElseThrow(LoginFailedException::new);

        // 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.password(), member.getPassword())) {
            throw new LoginFailedException();
        }

        // 토큰 생성
        String token = jwtUtil.generateToken(member.getEmail(), member.getRole().name());
        return new MemberResponseDto(token);
    }
}
