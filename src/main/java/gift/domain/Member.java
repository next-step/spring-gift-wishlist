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

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPwd() { return pwd; }
}
