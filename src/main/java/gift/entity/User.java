package gift.entity;

import java.time.LocalDateTime;

public record User(Long id, String email, String password, LocalDateTime createdDate, String role) {
    public User(String email, String password) {
        this(null, email, password, null, null);
    }
}
