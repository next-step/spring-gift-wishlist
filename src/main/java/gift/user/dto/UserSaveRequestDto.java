package gift.user.dto;

import gift.user.domain.User;
import jakarta.validation.constraints.NotNull;

public class UserSaveRequestDto {
    @NotNull
    String email;
    @NotNull
    String password;

    public UserSaveRequestDto() {};

    public UserSaveRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
