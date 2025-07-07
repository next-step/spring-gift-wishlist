package gift.dto.request;

import gift.entity.User;

public record ModifyUserRequest(
        String email,
        String password
) {
    public static User toEntity(ModifyUserRequest modifyUserRequest) {
        return new User(
            modifyUserRequest.email(),
            modifyUserRequest.password()
        );
    }
}
