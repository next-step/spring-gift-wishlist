package gift.domain;

public class Member {
    private Long id;
    private String email;
    private String password;

    public Member() {}

    public Member(Long id, String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }


}