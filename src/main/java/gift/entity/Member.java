package gift.entity;

public class Member {

    private Long id;
    private String email;
    private String password;
    private String role;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
