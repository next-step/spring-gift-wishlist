package gift.entity;

import gift.dto.UserRequestDto;
import java.time.LocalDateTime;

public record User(String email, String password, LocalDateTime createdDate, String authority) {
    public User(UserRequestDto userRequestDto) {
        this(userRequestDto.email(), userRequestDto.password(), null, null);
    }
}
