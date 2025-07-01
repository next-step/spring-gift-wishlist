package gift.dto.response;

public record MessageResponseDto<T>(boolean success, String message, int code, T data) {

}
