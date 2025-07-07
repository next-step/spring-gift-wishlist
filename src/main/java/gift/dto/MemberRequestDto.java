package gift.dto;

public class MemberRequestDto {
    String email;
    String password;

    public MemberRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }
}
