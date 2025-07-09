package gift.member.adapter.web;

import gift.common.annotation.RequireAdmin;
import gift.member.application.port.out.MemberPersistencePort;
import gift.member.domain.model.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final MemberPersistencePort memberPersistencePort;

    public AdminController(MemberPersistencePort memberPersistencePort) {
        this.memberPersistencePort = memberPersistencePort;
    }

    @RequireAdmin
    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberPersistencePort.findAll();
        return ResponseEntity.ok(members);
    }

    @RequireAdmin
    @PostMapping("/members")
    public ResponseEntity<Member> createMember(@RequestBody CreateMemberRequest request) {
        Member member = Member.create(request.email(), request.password());
        Member savedMember = memberPersistencePort.save(member);
        return ResponseEntity.status(201).body(savedMember);
    }

    @RequireAdmin
    @PutMapping("/members/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody UpdateMemberRequest request) {
        Member member = memberPersistencePort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        
        Member updatedMember = Member.of(
                member.getId(),
                request.email() != null ? request.email() : member.getEmail(),
                request.password() != null ? request.password() : member.getPassword(),
                request.role() != null ? request.role() : member.getRole(),
                member.getCreatedAt()
        );
        
        Member savedMember = memberPersistencePort.save(updatedMember);
        return ResponseEntity.ok(savedMember);
    }

    @RequireAdmin
    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        if (!memberPersistencePort.existsById(id)) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }
        memberPersistencePort.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public record CreateMemberRequest(String email, String password) {}
    public record UpdateMemberRequest(String email, String password, gift.member.domain.model.Role role) {}
} 