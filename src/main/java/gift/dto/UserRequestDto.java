package gift.dto;

import gift.entity.User;
import gift.entity.vo.Email;
import gift.entity.vo.Password;
import jakarta.validation.constraints.NotBlank;

public class UserRequestDto {

    private Email email;
    private Password password;

    public UserRequestDto(Email email, Password password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email.value();
    }

    public String getPassword() {
        return password.value();
    }

    public User toEntity() {
        return new User(email, password);
    }
}
