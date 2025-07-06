package gift.entity.product.value;

import java.util.Objects;

public record ProductId(Long value) {

    public ProductId {
        Objects.requireNonNull(value, "상품 ID는 null일 수 없습니다.");
        if (value < 1) {
            throw new IllegalArgumentException("상품 ID는 음수이거나 0일 수 없습니다.");
        }
    }
}
