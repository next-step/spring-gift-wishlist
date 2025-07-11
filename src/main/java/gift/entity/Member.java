package gift.entity;

public class Member {
    private Long id;
    private String email;
    private String password;
    private MemberRole role;

    public Member() {}

    public Member(Long id, String email, String password, MemberRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public MemberRole getRole() { return role; }
    public void setRole(MemberRole role) {this.role = role;}
}
    
