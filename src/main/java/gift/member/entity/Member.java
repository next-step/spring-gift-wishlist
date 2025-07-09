package gift.member.entity;

import gift.member.vo.Email;
import gift.member.vo.Password;

public class Member {
    private final Long id;
    private final Email email;
    private final Password password;

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public Member(Long id, Email email, Password password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
