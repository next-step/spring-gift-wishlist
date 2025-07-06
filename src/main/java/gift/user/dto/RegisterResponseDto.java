package gift.user.dto;

import gift.user.entity.User;

public record RegisterResponseDto(
    String token
) {
//TODO : token 로직 추가 시 수정
  public static RegisterResponseDto from(User user) {
    return new RegisterResponseDto(
        token
    );
  }
}

