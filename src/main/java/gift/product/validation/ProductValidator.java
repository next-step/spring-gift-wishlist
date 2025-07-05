package gift.product.validation;

import gift.global.validation.NameBlacklist;
import gift.global.validation.NameType;
import gift.product.exception.InvalidProductNameException;
import gift.product.exception.ProductErrorCode;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

  public void validateProductName(String productName) {
    List<String> blacklist = NameBlacklist.getBlacklist(NameType.PRODUCT);

    boolean containsBlacklistedWord = blacklist.stream()
        .anyMatch(productName::contains);

    if (containsBlacklistedWord) {
      List<String> foundWords = blacklist.stream()
          .filter(productName::contains)
          .toList();

      throw new InvalidProductNameException(
          "상품명에 다음 키워드를 포함할 수 없습니다: " + String.join(", ", foundWords),
          ProductErrorCode.INVALID_PRODUCT_NAME
      );
    }
  }

}
