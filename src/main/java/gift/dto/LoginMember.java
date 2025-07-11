package gift.dto;

public class LoginMember {
    private Long id;
    private String email;

    public LoginMember(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {return id;}

    public String getEmail() {return email;}

    public void setId(Long id) {this.id = id;}

    public void setEmail(String email) {this.email = email;}
}
