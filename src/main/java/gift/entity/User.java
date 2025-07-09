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

    public Email email() {
        return email;
    }

    public Password password() {
        return password;
    }
}
