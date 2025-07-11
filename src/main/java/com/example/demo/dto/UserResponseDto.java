package com.example.demo.dto;

public record UserResponseDto(
    String token,
    UserDataInfo user
) {
}
