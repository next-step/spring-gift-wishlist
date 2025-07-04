package gift.entity;

import gift.dto.UserRequestDto;
import java.time.LocalDateTime;

public record User(String id, String password, LocalDateTime createdDate, String authority) {
    public User(UserRequestDto userRequestDto) {
        this(userRequestDto.id(), userRequestDto.password(), null, null);
    }
}
