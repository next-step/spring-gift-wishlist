package gift.user.domain;

import java.util.UUID;

public class User {
    private UUID id;
    private String email;
    private String password;

    public User(UUID id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public boolean isEqualToPassword(String password) {
        return this.password.equals(password);
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
