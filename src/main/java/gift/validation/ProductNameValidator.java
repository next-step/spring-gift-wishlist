package gift.validation;

import gift.exception.InvalidRequestException;

public class ProductNameValidator {
  public static void validate(String name, boolean kakaoConfirmed) {
    if (name.contains("카카오") && !kakaoConfirmed) {
      throw new InvalidRequestException("상품 이름에 '카카오'를 포함하려면 MD 협의가 필요합니다.");
    }
  }
}
