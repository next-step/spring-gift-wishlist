package gift.entity;

public class Member {
    private Long id;
    final private String email;
    final private String password;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
