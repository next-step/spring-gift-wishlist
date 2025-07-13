package gift.wishlist.vo;

import gift.global.exception.InValidAmountException;

public class Amount {
    private final Integer value;

    public Amount(int value) {
        check(value);
        this.value = value;
    }

    private void check(Integer amount) {
        if (amount == null || amount > 99) {
            throw new InValidAmountException();
        }
    }

    public Integer getValue() {
        return value;
    }
}
