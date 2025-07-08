package gift.domain;

public class Member {

    private Long id;
    private String email;
    private String password;

    private Member() {}

    private Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static Member of(Long id, String email, String password) {
        return new Member(id, email, password);
    }

    public static Member of(String email, String password) {
        return new Member(null, email, password);
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
