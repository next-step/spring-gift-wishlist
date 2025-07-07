package gift.entity.product.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ProductImageUrlTest {

    @Test
    void validHttpUrl() {
        String urlStr = "http://example.com/image.png";
        ProductImageUrl url = new ProductImageUrl(urlStr);
        assertThat(url.url()).isEqualTo(urlStr);
    }

    @Test
    void validHttpsUrl() {
        String urlStr = "https://www.example.com/path/to/resource";
        ProductImageUrl url = new ProductImageUrl(urlStr);
        assertThat(url.url()).isEqualTo(urlStr);
    }

    @Test
    void nullUrlThrows() {
        assertThatThrownBy(() -> new ProductImageUrl(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("이미지 URL은 필수 입력값입니다.");
    }

    @Test
    void invalidPatternThrows() {
        assertThatThrownBy(() -> new ProductImageUrl("ftp://example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바른 HTTP/HTTPS 이미지 URL이어야 합니다.");
    }

    @Test
    void invalidSyntaxThrows() {
        assertThatThrownBy(() -> new ProductImageUrl("http://exa mple.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("올바른 HTTP/HTTPS 이미지 URL이어야 합니다.");
    }
}
