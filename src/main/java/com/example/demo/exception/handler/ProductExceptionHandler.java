package com.example.demo.exception.handler;

import com.example.demo.controller.product.ProductController;
import com.example.demo.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice(assignableTypes = {ProductController.class})
public class ProductExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDto> handleIllegalArgument(
      IllegalArgumentException ex,
      HttpServletRequest request
  ) {
    ErrorResponseDto errorResponseDto = new ErrorResponseDto(
        "https://example.com/product-not-found",
        "Product Not Found",
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        request.getRequestURI(),
        null
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
  }
}
