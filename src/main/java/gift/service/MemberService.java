package gift.service;

import gift.config.JwtUtil;
import gift.dto.MemberRequest;
import gift.dto.MemberResponse;
import gift.dto.TokenResponse;
import gift.entity.Member;
import gift.repository.MemberRepository;
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
        if (memberRepository.findAll().stream()
            .anyMatch(m -> m.getEmail().equals(request.email()))) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Member member = new Member(
            null,
            request.email(),
            request.password(),
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

        Member member = memberRepository.findAll().stream()
            .filter(m -> m.getEmail().equals(request.email()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("이메일이 올바르지 않습니다."));

        if (!member.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        String token = jwtUtil.generateToken(member);

        return new TokenResponse(token);
    }

    public TokenResponse updateMember(Long id, MemberRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Member existingMember = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Member(id: " + id + " ) not found"));

        String password = existingMember.getPassword();
        if (request.password() != null && !request.password().isEmpty()) {
            password = request.password();
        }

        Member updatedMember = new Member(
            id,
            existingMember.getEmail(),
            password,
            existingMember.getRole()
        );

        updatedMember = memberRepository.update(updatedMember);
        String token = jwtUtil.generateToken(updatedMember);

        return new TokenResponse(token);
    }

    public void deleteMember(Long id) {
        if (memberRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Member(id: " + id + " ) not found");
        }
        memberRepository.delete(id);
    }

    public MemberResponse getMember(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Member(id: " + id + " ) not found"));
        return new MemberResponse(
            id,
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
