package gift.entity;

public class Member {
    private Long id;
    private String email;
    private String password;

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password) {
        this(null, email, password);
    }

    Long getId() {
        return id;
    }

    String getEmail() {
        return email;
    }

    String getPassword() {
        return password;
    }
}
