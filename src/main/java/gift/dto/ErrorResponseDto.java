package gift.dto;

public record ErrorResponseDto(int status, String errorCode, String message) {

    public static ErrorResponseDto of(int status, String errorCode, String message) {
        return new ErrorResponseDto(status, errorCode, message);
    }

}
