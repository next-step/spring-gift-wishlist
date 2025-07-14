package gift.domain;

public class WishList {

    private Long id;
    private Long memberId;
    private Long productId;
    private int quantity;

    public WishList(Long id, Long memberId, Long productId, int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static WishList withId(Long id, Long memberId, Long productId, int quantity) {
        return new WishList(id, memberId, productId, quantity);
    }

    public static WishList withoutId(Long memberId, Long productId, int quantity) {
        return new WishList(null, memberId, productId, quantity);
    }

    public Long id() {
        return id;
    }

    public Long memberId() {
        return memberId;
    }

    public Long productId() {
        return productId;
    }

    public int quantity() {
        return quantity;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void changeQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
        }
        this.quantity = quantity;
    }

    public void addQuantity(int additionalQuantity) {
        if (additionalQuantity <= 0) {
            throw new IllegalArgumentException("추가 수량은 양수여야 합니다.");
        }
        this.quantity += additionalQuantity;
    }
}
