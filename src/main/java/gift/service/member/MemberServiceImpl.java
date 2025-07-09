package gift.service.member;

import gift.dto.member.AuthResponse;
import gift.dto.member.RegisterRequest;
import gift.entity.member.Member;
import gift.exception.MemberNotFoundException;
import gift.repository.member.MemberRepository;
import gift.util.JwtUtil;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberDao) {
        this.memberRepository = memberDao;
    }

    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthResponse register(RegisterRequest req) {
        String hash = sha256(req.password());
        Member m = Member.register(req.email(), hash);
        m = memberRepository.save(m);

        String token = JwtUtil.generateToken(m.getId().id(), m.getRole().name());
        return new AuthResponse(token);
    }

    public AuthResponse login(String email, String rawPassword) {
        Member m = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));
        String hash = sha256(rawPassword);
        if (!m.getPassword().password().equals(hash)) {
            throw new MemberNotFoundException(email);
        }
        String token = JwtUtil.generateToken(m.getId().id(), m.getRole().name());
        return new AuthResponse(token);
    }
}
