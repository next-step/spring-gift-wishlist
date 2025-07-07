package gift.entity;

import java.time.LocalDateTime;

public record User(String email, String password, LocalDateTime createdDate) {
    public User(String email, String password) {
        this(email, password, null);
    }
}
