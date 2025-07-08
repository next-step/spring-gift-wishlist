package gift.service;

import gift.config.JwtUtil;
import gift.dto.MemberRequest;
import gift.dto.MemberResponse;
import gift.dto.TokenResponse;
import gift.entity.Member;
import gift.exception.DuplicateEmailException;
import gift.exception.InvalidPasswordException;
import gift.exception.MemberNotFoundException;
import gift.repository.MemberRepository;
import java.util.Base64;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public TokenResponse register(MemberRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = Base64.getEncoder().encodeToString(request.password().getBytes());
        Member member = new Member(
            null,
            request.email(),
            encodedPassword,
            request.role()
        );

        member = memberRepository.save(member);
        String token = jwtUtil.generateToken(member);

        return new TokenResponse(token);
    }

    public TokenResponse login(MemberRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Member member = memberRepository.findByEmail(request.email())
            .orElseThrow(() -> new MemberNotFoundException("이메일이 올바르지 않습니다."));

        String encodedPassword = Base64.getEncoder().encodeToString(request.password().getBytes());
        if (!member.getPassword().equals(encodedPassword)) {
            throw new InvalidPasswordException("비밀번호가 올바르지 않습니다.");
        }

        String token = jwtUtil.generateToken(member);

        return new TokenResponse(token);
    }

    public TokenResponse updateMember(MemberRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        String email = request.email();
        Member existingMember = memberRepository.findByEmail(email)
            .orElseThrow(
                () -> new MemberNotFoundException("Member(email: " + email + " ) not found"));

        String password = existingMember.getPassword();
        if (request.password() != null && !request.password().isEmpty()) {
            password = Base64.getEncoder().encodeToString(request.password().getBytes());
        }

        Member updatedMember = new Member(
            existingMember.getId(),
            existingMember.getEmail(),
            password,
            existingMember.getRole()
        );

        updatedMember = memberRepository.update(updatedMember);
        String token = jwtUtil.generateToken(updatedMember);

        return new TokenResponse(token);
    }

    public void deleteMember(String email) {
        if (memberRepository.findByEmail(email).isEmpty()) {
            throw new MemberNotFoundException("Member(email: " + email + " ) not found");
        }
        memberRepository.delete(email);
    }

    public MemberResponse getMember(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(
                () -> new MemberNotFoundException("Member(email: " + email + " ) not found"));
        return new MemberResponse(
            member.getId(),
            member.getEmail(),
            member.getPassword(),
            member.getRole()
        );
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
            .map(member -> new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getRole()
            ))
            .toList();
    }

}
