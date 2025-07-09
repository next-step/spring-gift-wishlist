// gift/service/member/MemberServiceImpl.java
package gift.service.member;

import gift.dto.member.AuthRequest;
import gift.dto.member.AuthResponse;
import gift.entity.member.Member;
import gift.entity.member.value.Role;
import gift.exception.MemberNotFoundException;
import gift.repository.member.MemberRepository;
import gift.util.JwtUtil;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberDao) {
        this.memberRepository = memberDao;
    }

    public static String sha256(String input) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            var bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            var sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthResponse register(AuthRequest req) {
        String hash = sha256(req.password());
        Member m = Member.register(req.email(), hash);
        m = memberRepository.register(m);
        String token = JwtUtil.generateToken(m.getId().id(), m.getRole().name());
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(String email, String rawPassword) {
        Member m = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));
        if (!m.getPassword().password().equals(sha256(rawPassword))) {
            throw new MemberNotFoundException(email);
        }
        String token = JwtUtil.generateToken(m.getId().id(), m.getRole().name());
        return new AuthResponse(token);
    }

    private void checkAdmin(String role) {
        if (!"ADMIN".equals(role)) {
            throw new IllegalArgumentException("관리자 권한이 필요합니다.");
        }
    }

    @Override
    public List<Member> getAllMembers(String role) {
        checkAdmin(role);
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> getMemberById(Long id, String role) {
        checkAdmin(role);
        return memberRepository.findById(id);
    }

    @Override
    public Member createMember(
            String email, String rawPassword, String newRole, String role) {
        checkAdmin(role);
        String hash = sha256(rawPassword);
        Member m = Member.register(email, hash)
                .withRole(Role.valueOf(newRole));
        return memberRepository.register(m);
    }

    @Override
    public Member updateMember(
            Long id,
            String email,
            String rawPassword,
            String newRole,
            String role) {
        checkAdmin(role);
        Member existing = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id.toString()));
        String hash = rawPassword != null && !rawPassword.isBlank()
                ? sha256(rawPassword)
                : existing.getPassword().password();
        Member updated = existing.withEmail(email)
                .withPasswordHash(hash)
                .withRole(Role.valueOf(newRole));
        return memberRepository.updateMember(updated);
    }

    @Override
    public void deleteMember(Long id, String role) {
        checkAdmin(role);
        memberRepository.deleteById(id);
    }
}
