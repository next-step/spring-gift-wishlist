package gift.service;

import gift.dto.*;
import gift.entity.Member;
import gift.entity.Role;
import gift.exception.LoginFailedException;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, JwtTokenService jwtTokenService, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthTokenResponseDTO register(MemberRequestDTO request) {
        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Member member = new Member(0, request.email(), encodedPassword, request.role());
        Member savedMember = memberRepository.register(member);

        String token = jwtTokenService.generateJwtToken(savedMember);

        return new AuthTokenResponseDTO(token);
    }

    public AuthTokenResponseDTO login(MemberLoginRequestDTO request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new LoginFailedException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new LoginFailedException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenService.generateJwtToken(member);

        return new AuthTokenResponseDTO(token);
    }

    public List<MemberResponseDTO> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(member -> new MemberResponseDTO(
                        member.getId(),
                        member.getEmail(),
                        member.getRole()
                ))
                .collect(Collectors.toList());
    }

    public MemberResponseDTO getMemberById(Integer id) {
        Member member = memberRepository.findById(id);

        return new MemberResponseDTO(
                member.getId(),
                member.getEmail(),
                member.getRole()
        );
    }

    @Transactional
    public MemberResponseDTO update(Integer id, MemberRequestDTO request) {
        String encodedPassword = passwordEncoder.encode(request.password());

        Member updated = new Member(
                id,
                request.email(),
                encodedPassword,
                request.role()
        );

        if (memberRepository.update(id, updated) == 0) {
            throw new IllegalArgumentException("Member Not Found");
        }

        return new MemberResponseDTO(
                updated.getId(),
                updated.getEmail(),
                updated.getRole()
        );
    }

    @Transactional
    public void delete(Integer id) {
        if (memberRepository.delete(id) == 0) {
            throw new IllegalArgumentException("Member Not Found");
        }
    }
}
