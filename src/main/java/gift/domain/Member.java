package gift.domain;

public class Member {
    private Long id;
    private String email;
    private String pwd;

    public Member(Long id, String email, String pwd){
        this.id = id;
        this.email = email;
        this.pwd = pwd;
    }

    public Long id() { return id; }
    public String email() { return email; }
    public String pwd() { return pwd; }
}
