package gift.entity.product.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ProductPriceTest {

    @Test
    void validPrice() {
        ProductPrice price = new ProductPrice(1);
        assertThat(price.value()).isEqualTo(1);
    }

    @Test
    void belowMinPriceThrows() {
        assertThatThrownBy(() -> new ProductPrice(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 1원 이상이어야 합니다.");
        assertThatThrownBy(() -> new ProductPrice(-100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 1원 이상이어야 합니다.");
    }
}
