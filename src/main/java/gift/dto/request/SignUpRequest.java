package gift.dto.request;

import gift.entity.User;
import jakarta.validation.constraints.Email;

public record SignUpRequest(
        @Email
        String email,
        String password
) {
        public User toEntity() {
                return new User(this.email(), this.password());
        }
}
