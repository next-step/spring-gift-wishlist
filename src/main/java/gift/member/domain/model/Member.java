package gift.member.domain.model;

import java.time.LocalDateTime;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final Role role;
    private final LocalDateTime createdAt;

    public Member(Long id, String email, String password, Role role, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role == null ? Role.USER : role;
        this.createdAt = createdAt;
    }
    
    public static Member of(Long id, String email, String password, Role role, LocalDateTime createdAt) {
        return new Member(id, email, password, role, createdAt);
    }

    public static Member create(String email, String password) {
        return new Member(null, email, password, Role.USER, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
} 