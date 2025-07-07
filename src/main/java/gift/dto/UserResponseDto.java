package gift.dto;

import gift.entity.User;

import java.time.LocalDateTime;

public record UserResponseDto(String email, String password, LocalDateTime createdDate) {
    public UserResponseDto(User user) {
        this(user.email(), user.password(), user.createdDate());
    }
}
