package gift.member.entity;

public class Member {
    private Long id;
    private String email;
    private String password;
    private String role;

    public Member(Long id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {return id;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public String getRole() {return role;}
}
