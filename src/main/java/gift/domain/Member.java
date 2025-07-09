package gift.domain;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final String role;

    private Member(Long id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member of(Long id, String email, String password, String role) {
        return new Member(id, email, password, role);
    }

    public static Member of(Long id, String email, String password) {
        return new Member(id, email, password, "ROLE_USER");
    }

    public static Member of(String email, String password) {
        return new Member(null, email, password, "ROLE_USER");
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
        return role;
    }
}
