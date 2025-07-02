package com.example.demo.controller;

import com.example.demo.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> handleValidationErrors(
      MethodArgumentNotValidException ex,
      HttpServletRequest request
  ) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
      .getFieldErrors()
      .forEach(error ->
          errors.put(error.getField(), error.getDefaultMessage())
      );

    ErrorResponseDto errorResponseDto = new ErrorResponseDto(
        "https://example.com/validation-error",     // type (URI)
        "Validation Failed",                        // title
        HttpStatus.BAD_REQUEST.value(),             // status
        "입력값이 유효하지 않습니다.",                 // detail
        request.getRequestURI(),                    // instance
        errors                                      // field errors
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponseDto);
  }
}
