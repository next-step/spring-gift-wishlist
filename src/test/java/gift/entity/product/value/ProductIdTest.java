package gift.entity.product.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ProductIdTest {

    @Test
    void validId() {
        ProductId id = new ProductId(1L);
        assertThat(id.id()).isEqualTo(1L);
    }

    @Test
    void nullIdThrows() {
        assertThatThrownBy(() -> new ProductId(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("상품 ID는 null일 수 없습니다.");
    }

    @Test
    void zeroOrNegativeIdThrows() {
        assertThatThrownBy(() -> new ProductId(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 ID는 음수이거나 0일 수 없습니다.");
        assertThatThrownBy(() -> new ProductId(-5L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 ID는 음수이거나 0일 수 없습니다.");
    }
}
