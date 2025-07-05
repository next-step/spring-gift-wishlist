package gift.dto;

public class MemberRequestDto {

    private String email;
    private String password;
    private String role;

    public MemberRequestDto() {
        this.role = "USER";
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }


}
