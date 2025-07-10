package gift.user.dto;

import gift.user.domain.User;

public class UserPatchRequestDto {
    String email;
    String password;

    public UserPatchRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserPatchRequestDto(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
