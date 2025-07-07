package gift.entity;

public class Member {
    private Long id;
    private String password;
    private String email;
    private Role role;

    public Member(Long id, String password, String email, Role role) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
