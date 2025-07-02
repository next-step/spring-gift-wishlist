package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoKakaoValidator implements ConstraintValidator<NoKakao, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if (value == null) return true;
    return !value.contains("카카오");
  }
}
