package gift.entity.product.value;

public record ProductPrice(int value) {

    public static final int MIN_PRICE = 1;

    public ProductPrice {
        if (value < MIN_PRICE) {
            throw new IllegalArgumentException("가격은 1원 이상이어야 합니다.");
        }
    }
}
