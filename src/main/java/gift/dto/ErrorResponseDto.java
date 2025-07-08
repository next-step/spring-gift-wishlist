package gift.dto;

public record ErrorResponseDto(int status, String errorCode, String message) {
}
