package gift.validation;

import gift.dto.ProductRequestDto;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProductValidatorTest {

    private ProductValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new ProductValidator();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);
    }

    @Test
    @DisplayName("정상적인 상품명은 유효함")
    void validProductName() {
        // given
        ProductRequestDto dto = new ProductRequestDto("정상 상품명", 1000, "image.jpg");

        // when
        boolean result = validator.isValid(dto, context);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("null 상품명은 유효하지 않음")
    void nullProductName() {
        // given
        ProductRequestDto dto = new ProductRequestDto(null, 1000, "image.jpg");

        // when
        boolean result = validator.isValid(dto, context);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("빈 상품명은 유효하지 않음")
    void emptyProductName() {
        // given
        ProductRequestDto dto = new ProductRequestDto("", 1000, "image.jpg");

        // when
        boolean result = validator.isValid(dto, context);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("공백만 있는 상품명은 유효하지 않음")
    void blankProductName() {
        // given
        ProductRequestDto dto = new ProductRequestDto("   ", 1000, "image.jpg");

        // when
        boolean result = validator.isValid(dto, context);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("15자를 초과하는 상품명은 유효하지 않음")
    void tooLongProductName() {
        // given
        ProductRequestDto dto = new ProductRequestDto("이것은15자를초과하는매우긴상품명입니다", 1000, "image.jpg");

        // when
        boolean result = validator.isValid(dto, context);

        // then
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"!", "@", "#", "$", "%", "^", "*"})
    @DisplayName("허용되지 않은 특수문자가 포함된 상품명은 유효하지 않음")
    void invalidSpecialCharacters(String productName) {
        // given
        ProductRequestDto dto = new ProductRequestDto(productName, 1000, "image.jpg");

        // when
        boolean result = validator.isValid(dto, context);

        // then
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"(설명)", "[옵션]", "+플러스", "-마이너스", "&", "/슬래시", "_"})
    @DisplayName("허용된 특수문자가 포함된 상품명은 유효함")
    void validSpecialCharacters(String productName) {
        // given
        ProductRequestDto dto = new ProductRequestDto(productName, 1000, "image.jpg");

        // when
        boolean result = validator.isValid(dto, context);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("카카오가 포함된 상품명은 유효하지 않음")
    void kakaoIncludedProductName() {
        // given
        ProductRequestDto dto = new ProductRequestDto("카카오 상품", 1000, "image.jpg");

        // when
        boolean result = validator.isValid(dto, context);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("카카오가 포함되지 않은 상품명은 유효함")
    void kakaoNotIncludedProductName() {
        // given
        ProductRequestDto dto = new ProductRequestDto("일반 상품", 1000, "image.jpg");

        // when
        boolean result = validator.isValid(dto, context);

        // then
        assertThat(result).isTrue();
    }
}