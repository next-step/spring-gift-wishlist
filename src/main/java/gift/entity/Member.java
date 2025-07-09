package gift.entity;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final String role;

    public Member(Long id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
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

    @Override
    public String toString() {
        return "Member(" + id + ") - email: " + email;
    }

}
