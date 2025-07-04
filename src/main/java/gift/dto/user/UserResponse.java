package gift.dto.user;

import gift.entity.User;

public record UserResponse(
    Long id,
    String email
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(user.getId(), user.getEmail());
    }
}