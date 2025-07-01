package gift.exception;

import java.time.LocalDateTime;

public record ExceptionResponseDto(String errMessage, int status, LocalDateTime timeStamp) {

}
