package gift.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record MemberRegisterRequestDto(
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "유효하지 않은 이메일 형식입니다.")
        String email,

        @NotBlank(message = "이름은 필수 입력값입니다.")
        String name,

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Length(min = 8, message = "비밀번호는 8자 이상으로 입력해주세요.")
        String password
) {

}
