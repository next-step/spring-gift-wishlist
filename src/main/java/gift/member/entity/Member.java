package gift.member.entity;

import gift.member.vo.Name;
import gift.member.vo.Email;
import gift.member.vo.Password;

public class Member {
    private final Long id;
    private final Name name;
    private final Email email;
    private final Password password;

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public Member(Long id, Name name, Email email, Password password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
