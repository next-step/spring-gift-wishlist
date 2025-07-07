package gift.dto;

import gift.entity.User;

public class UserRequestDto {

    private String email;
    private String password;

    public UserRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User toEntity() {
        return new User(email, password);
    }
}
