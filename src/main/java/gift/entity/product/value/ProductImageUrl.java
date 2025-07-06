package gift.entity.product.value;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.regex.Pattern;

public record ProductImageUrl(String value) {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(?:http|https)://[\\w.-]+(?:\\.[\\w.-]+)+(?:/\\S*)?$",
            Pattern.CASE_INSENSITIVE
    );

    public ProductImageUrl {
        Objects.requireNonNull(value, "이미지 URL은 필수 입력값입니다.");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("이미지 URL은 필수 입력값입니다.");
        }
        if (!URL_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("올바른 HTTP/HTTPS 이미지 URL이어야 합니다.");
        }
        try {
            URI uri = new URI(trimmed);
            String scheme = uri.getScheme().toLowerCase();
            if (!"http".equals(scheme) && !"https".equals(scheme)) {
                throw new IllegalArgumentException("이미지 URL은 HTTP 또는 HTTPS 프로토콜이어야 합니다.");
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("올바른 이미지 URL이어야 합니다.", e);
        }
        value = trimmed;
    }
}
