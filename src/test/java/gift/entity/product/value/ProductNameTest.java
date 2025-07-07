package gift.entity.product.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ProductNameTest {

    @Test
    void validName() {
        ProductName name = new ProductName("상품 A");
        assertThat(name.name()).isEqualTo("상품 A");
    }

    @Test
    void nullNameThrows() {
        assertThatThrownBy(() -> new ProductName(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("상품 이름은 필수 입력값입니다.");
    }

    @Test
    void emptyNameThrows() {
        assertThatThrownBy(() -> new ProductName("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 이름은 필수 입력값입니다.");
    }

    @Test
    void tooLongNameThrows() {
        String longName = "A".repeat(ProductName.MAX_LENGTH + 1);
        assertThatThrownBy(() -> new ProductName(longName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("최대 " + ProductName.MAX_LENGTH + "자까지 입력할 수 있습니다.");
    }

    @Test
    void invalidCharsThrows() {
        assertThatThrownBy(() -> new ProductName("Invalid@Name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 이름에 허용되지 않는 문자가 포함되었습니다.");
    }
}
