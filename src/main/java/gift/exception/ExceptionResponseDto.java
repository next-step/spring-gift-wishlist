package gift.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ExceptionResponseDto(
    List<String> errMessages,
    int status,
    LocalDateTime timeStamp) {

}
