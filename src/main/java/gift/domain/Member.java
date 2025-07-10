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

    public static Member createMemberWithEmailAndPassword(String email, String password) {
        return new Member(null, email, password, "ROLE_USER");
    }

    public static Member createMemberForUpdate(Long id, String email) {
        return new Member(id, email, null, null);
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
