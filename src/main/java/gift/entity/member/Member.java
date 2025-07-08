package gift.entity.member;

import gift.entity.member.value.MemberEmail;
import gift.entity.member.value.MemberId;
import gift.entity.member.value.MemberPasswordHash;
import gift.entity.member.value.MemberRole;
import gift.entity.member.value.Role;
import java.time.LocalDateTime;
import java.util.Objects;

public class Member {

    private final MemberId id;
    private final MemberEmail email;
    private final MemberPasswordHash passwordHash;
    private final Role role;
    private final LocalDateTime createdAt;

    private Member(
            MemberId id,
            MemberEmail email,
            MemberPasswordHash passwordHash,
            Role role,
            LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static Member register(String email, String rawPasswordHash) {
        return new Member(
                null,
                new MemberEmail(email),
                new MemberPasswordHash(rawPasswordHash),
                Role.USER,
                LocalDateTime.now()
        );
    }

    /**
     * DB 조회용: 모든 필드를 검증하여 생성 MemberId(null) 허용하려면 생성자 null 체크에서 제외하거나 아래처럼 분리
     */
    public static Member of(
            Long id,
            String email,
            String passwordHash,
            String roleInput,
            LocalDateTime createdAt) {
        return new Member(
                id != null ? new MemberId(id) : null,
                new MemberEmail(email),
                new MemberPasswordHash(passwordHash),
                new MemberRole(roleInput).role(),
                createdAt
        );
    }

    /**
     * DAO 저장 후 ID가 생겼을 때 호출
     */
    public Member withId(Long newId) {
        return new Member(
                new MemberId(newId),
                this.email,
                this.passwordHash,
                this.role,
                this.createdAt
        );
    }

    /**
     * 비밀번호 해시만 교체
     */
    public Member withPasswordHash(String newPasswordHash) {
        return new Member(
                this.id,
                this.email,
                new MemberPasswordHash(newPasswordHash),
                this.role,
                this.createdAt
        );
    }

    /**
     * 역할만 교체
     */
    public Member withRole(Role newRole) {
        return new Member(
                this.id,
                this.email,
                this.passwordHash,
                newRole,
                this.createdAt
        );
    }

    public MemberId getId() {
        return id;
    }

    public MemberEmail getEmail() {
        return email;
    }

    public MemberPasswordHash getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
