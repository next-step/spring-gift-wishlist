package gift.user.dto;

import gift.user.entity.User;

public record RegisterResponseDto(
    String token
) {

  public static RegisterResponseDto from(String token) {
    return new RegisterResponseDto(
        token
    );
  }
}

