package gift.entity;

public class Member {
    private Long id;
    private String email;
    private String password;
    private String Role;

    public Member(Long id, String email, String password, String Role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.Role = Role;
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

    public String getRole() {
        return Role;
    }
}
