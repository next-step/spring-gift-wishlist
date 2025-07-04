package gift.dto;

import java.time.LocalDateTime;

public record UserResponseDto(String id, String password, LocalDateTime createdDate) {

}
