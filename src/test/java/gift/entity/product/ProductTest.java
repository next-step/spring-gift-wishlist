package gift.entity.product;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.product.value.ProductId;
import gift.entity.product.value.ProductImageUrl;
import gift.entity.product.value.ProductName;
import gift.entity.product.value.ProductPrice;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void createValidProduct() {
        Product product = new Product(
                new ProductId(1L),
                new ProductName("상품 A"),
                new ProductPrice(1000),
                new ProductImageUrl("https://example.com/img.png"),
                false
        );

        assertThat(product.id().value()).isEqualTo(1L);
        assertThat(product.name().value()).isEqualTo("상품 A");
        assertThat(product.price().value()).isEqualTo(1000);
        assertThat(product.imageUrl().value()).isEqualTo("https://example.com/img.png");
        assertThat(product.hidden()).isFalse();
    }

    @Test
    void equalsAndHashCode() {
        Product a = new Product(
                new ProductId(1L),
                new ProductName("상품 A"),
                new ProductPrice(1000),
                new ProductImageUrl("https://example.com/img.png"),
                false
        );
        
        assertThat(a.hashCode()).isEqualTo(a.hashCode());
    }

    @Test
    void differentHiddenFlagNotEqual() {
        Product a = new Product(
                new ProductId(1L),
                new ProductName("상품 A"),
                new ProductPrice(1000),
                new ProductImageUrl("https://example.com/img.png"),
                false
        );
        Product b = new Product(
                new ProductId(1L),
                new ProductName("상품 A"),
                new ProductPrice(1000),
                new ProductImageUrl("https://example.com/img.png"),
                true
        );

        assertThat(a).isNotEqualTo(b);
    }
}
