package gift.dto.user;

import gift.entity.User;
import gift.common.validation.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
        @NotNull(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @NotNull(message = "비밀번호는 필수입니다.")
        @ValidPassword
        String password
) {
        public User toEntity() {
                return new User(null, email, password);
        }
}
