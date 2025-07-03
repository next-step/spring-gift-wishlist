package gift.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void normalProductPass() {
        ProductRequest request = new ProductRequest(
            "Product(1+1)",
            1000,
            "test.com"
        );
        var violations = validator.validate(request);
        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    void imageNullProductPass() {
        ProductRequest request = new ProductRequest(
            "Product(1+1)",
            1000,
            null
        );
        var violations = validator.validate(request);
        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    void nameKakaoProductFail() {
        ProductRequest request = new ProductRequest(
            "카카오 우산",
            10000,
            "test.com"
        );
        var violations = validator.validate(request);
        Assertions.assertThat(violations)
            .anyMatch(v -> v.getMessage().contains("상품명에 '카카오'가 포함되었습니다. 담당자와 협의가 필요합니다."));
    }

    @Test
    void nameInvalidProductFail() {
        ProductRequest request = new ProductRequest(
            "달팽이@ 크림",
            5000,
            "test.com"
        );
        var violations = validator.validate(request);
        Assertions.assertThat(violations)
            .anyMatch(v -> v.getMessage().contains("허용되지 않은 특수 문자가 포함되었습니다."));
    }

    @Test
    void nameLen16ProductFail() {
        ProductRequest request = new ProductRequest(
            "1234567890abcdef",
            1000,
            "test.com"
        );
        var violations = validator.validate(request);
        Assertions.assertThat(violations)
            .anyMatch(v -> v.getMessage().contains("상품 이름은 최대 15자까지 입력 가능합니다."));
    }

    @Test
    void nameNullProductFail() {
        ProductRequest request = new ProductRequest(
            null,
            1000,
            "test.com"
        );
        var violations = validator.validate(request);
        Assertions.assertThat(violations)
            .anyMatch(v -> v.getMessage().contains("상품명은 필수입니다."));
    }

    @Test
    void priceNullProductFail() {
        ProductRequest request = new ProductRequest(
            "Test Product",
            null,
            "test.com"
        );
        var violations = validator.validate(request);
        Assertions.assertThat(violations)
            .anyMatch(v -> v.getMessage().contains("가격은 필수입니다."));
    }

}
