package gift.dto.user;

import gift.entity.User;
import gift.entity.UserRole;

import java.util.List;

public record UserAdminResponse(
    Long id,
    String email,
    String password,
    List<String> roles
) {
    public static UserAdminResponse from(User user) {
        return new UserAdminResponse(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(UserRole::toString)
                        .toList()
        );
    }
}
