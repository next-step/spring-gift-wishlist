package gift.dto;

public class MemberRequestDto {
    private String email;
    private String password;

    public MemberRequestDto() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
}
