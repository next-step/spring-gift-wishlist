package gift.member.domain.model;

import java.time.LocalDateTime;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final LocalDateTime createdAt;

    public Member(Long id, String email, String password, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }
    public static Member of(Long id, String email, String password, LocalDateTime createdAt) {
        return new Member(id, email, password, createdAt);
    }

    public static Member create(String email, String password) {
        return new Member(null, email, password, LocalDateTime.now());
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
} 