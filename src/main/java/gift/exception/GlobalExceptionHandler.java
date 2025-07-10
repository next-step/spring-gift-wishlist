package gift.exception;

import gift.dto.ErrorResponseDto;
import gift.dto.WishlistItemRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Void> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(OperationFailedException.class)
    public ResponseEntity<ErrorResponseDto> handleOperationFailed(OperationFailedException ex) {
        ErrorResponseDto responseDto = ErrorResponseDto.of(
                HttpStatus.NOT_FOUND.value(),
                "Operation Failed",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidProduct(InvalidProductException ex) {
        ErrorResponseDto responseDto = ErrorResponseDto.of(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Product",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Void> handleMemberNotFound(MemberNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(InvalidMemberException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidMember(InvalidMemberException ex) {
        ErrorResponseDto responseDto = ErrorResponseDto.of(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Member",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorized(UnauthorizedException ex) {
        ErrorResponseDto responseDto = ErrorResponseDto.of(
                HttpStatus.UNAUTHORIZED.value(),
                "UnAuthorized",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDto> handleForbidden(ForbiddenException ex) {
        ErrorResponseDto responseDto = ErrorResponseDto.of(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
    }

    @ExceptionHandler(WishlistItemNotFoundException.class)
    public ResponseEntity<Void> handleWishlistItemNotFound(WishlistItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
