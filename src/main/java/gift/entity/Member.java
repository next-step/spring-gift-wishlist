package gift.entity;

public class Member {

    private Long id;
    private String email;
    private String password;
    private String role;

    public Member() {}

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(Long id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

}
