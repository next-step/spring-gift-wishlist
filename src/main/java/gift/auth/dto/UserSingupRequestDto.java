package gift.auth.dto;

import jakarta.validation.constraints.NotNull;

public class UserSingupRequestDto {
    @NotNull
    private String email;
    @NotNull
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
