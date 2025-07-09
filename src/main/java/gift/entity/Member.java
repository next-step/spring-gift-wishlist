package gift.entity;

import gift.domain.member.Email;
import gift.domain.member.Password;
import gift.domain.member.Role;

public class Member {
    private final Long id;
    private Email email;
    private Password password;
    private final Role role;

    public Member(Email email, Password password) {
        this.id = null;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }

    public Member(Long id, Email email, Password password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member withId(Long id) {
        return new Member(id, this.email, this.password, this.role);
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}