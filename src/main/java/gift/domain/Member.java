package gift.domain;

public class Member {
    private Long id;
    private String email;
    private String password;

    public Member(Long id, String email, String pssword){
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Long id() { return id; }
    public String email() { return email; }
    public String password() { return password; }
}
