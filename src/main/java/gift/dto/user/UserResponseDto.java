package gift.dto.user;

import gift.model.User;

import java.time.LocalDateTime;

public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;

    public static UserResponseDto fromEntity(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.id = user.getId();
        dto.name = user.getName();
        dto.email = user.getEmail();
        dto.createdAt = user.getCreatedAt();
        return dto;
    }
}
