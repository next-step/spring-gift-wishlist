package gift.entity;

import gift.entity.vo.Email;
import gift.entity.vo.Password;

public class User {

    private Long id;
    private final Email email;
    private final Password password;

    protected User() {
        this.email = null;
        this.password = null;
    }

    public User(Email email, Password password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email.value();
    }

    public String getPassword() {
        return password.value();
    }
}
