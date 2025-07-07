package gift.dto;

import gift.domain.User;

public record AdminUserResponse(Long id, String email) {

    public static AdminUserResponse from(User user) {
        return new AdminUserResponse(user.getId(), user.getEmail());
    }
}
