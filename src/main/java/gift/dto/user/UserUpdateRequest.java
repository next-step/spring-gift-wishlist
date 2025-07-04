package gift.dto.user;

import gift.entity.User;
import gift.common.validation.annotation.ValidPassword;

public record UserUpdateRequest(
        @ValidPassword
        String password
) {
        public User toEntity() {
                return new User(null, null, password);
        }
}
