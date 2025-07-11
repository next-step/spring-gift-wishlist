package gift.dto.user;

import gift.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequestDto {

    @NotBlank(message = "이메일 입력은 필수입니다.")
    @Email(message = "이메일 형식을 맞춰주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 최소 8자리를 입력해주세요.")
    private String password;

    private String name;

    public UserRequestDto(String mail, String password, String name) {
        this.email = mail;
        this.password = password;
        this.name = name;
    }

    public User toEntity() {
        return new User(this.email, this.name);
    }

    //Getters
    public String getEmail() {return this.email;}
    public String getPassword() {return this.password;}
    public String getName() {return this.name;}
}
