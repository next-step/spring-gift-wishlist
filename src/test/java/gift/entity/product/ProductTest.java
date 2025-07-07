package gift.entity.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void createWithAllFields() {
        Product product = Product.of(1L, "상품 A", 1000, "https://example.com/img.png", true);
        assertThat(product.id().value()).isEqualTo(1L);
        assertThat(product.name().value()).isEqualTo("상품 A");
        assertThat(product.price().value()).isEqualTo(1000);
        assertThat(product.imageUrl().value()).isEqualTo("https://example.com/img.png");
        assertThat(product.hidden()).isTrue();
    }

    @Test
    void createWithoutHidden_defaultsToFalse() {
        Product product = Product.of(2L, "상품 B", 2000, "https://example.com/img2.png");
        assertThat(product.hidden()).isFalse();
    }

    @Test
    void createWithoutId_idIsNull() {
        Product product = Product.of("상품 C", 3000, "https://example.com/img3.png");
        assertThat(product.id()).isNull();
    }

    @Test
    void withMethods_updateOnlySpecifiedField() {
        Product original = Product.of(1L, "상품 A", 1000, "https://example.com/img.png", false);
        Product byId = original.withId(10L);
        assertThat(byId.id().value()).isEqualTo(10L);
        Product byName = original.withName("상품 X");
        assertThat(byName.name().value()).isEqualTo("상품 X");
        Product byPrice = original.withPrice(1500);
        assertThat(byPrice.price().value()).isEqualTo(1500);
        Product byImage = original.withImageUrl("https://example.com/imgNew.png");
        assertThat(byImage.imageUrl().value()).isEqualTo("https://example.com/imgNew.png");
        Product byHidden = original.withHidden(true);
        assertThat(byHidden.hidden()).isTrue();
    }

    @Test
    void equalsBasedOnId() {
        Product a = Product.of(1L, "A", 100, "https://example.com/imgNew.png", false);
        Product b = Product.of(1L, "B", 200, "https://example.com/imgNew.png", true);
        assertThat(a).isEqualTo(b);
    }

    @Test
    void notEqualsWhenDifferentId() {
        Product a = Product.of(1L, "A", 100, "https://example.com/imgNew.png", false);
        Product b = Product.of(2L, "A", 100, "https://example.com/imgNew.png", false);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void hashCodeBasedOnId() {
        Product a = Product.of(1L, "A", 100, "https://example.com/imgNew.png", false);
        Product b = Product.of(1L, "B", 200, "https://example.com/imgNew.png", true);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
