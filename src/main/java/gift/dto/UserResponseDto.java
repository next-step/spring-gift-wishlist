package gift.dto;

import java.time.LocalDateTime;

public record UserResponseDto(String email, String password, LocalDateTime createdDate) {
}
