package gift.dto;

import jakarta.validation.constraints.Email;

public class MemberRequestDto {

    @Email
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
