package gift.dto.request;

import gift.entity.User;

public record UserModifyRequest(
        String email,
        String password
) {
    public User toEntity() {
        return new User(
            this.email(),
            this.password()
        );
    }
}
