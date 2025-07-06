package gift.dto.request;

import gift.entity.User;
import jakarta.validation.constraints.Email;

public record SignUpRequest(
        @Email
        String email,
        String password
) {
        public static User toEntity(SignUpRequest signUpRequest) {
                return new User(
                        signUpRequest.email,
                        signUpRequest.password
                );
        }
}
