package gift.exception;


public record ErrorResponse(
    ErrorCode errorCode,
    String message
) {}
