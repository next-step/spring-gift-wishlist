package gift.entity;

public class Wishlist {
    private Long userId;
    private Long giftId;

    public Wishlist(Long userId, Long giftId) {
        this.userId = userId;
        this.giftId = giftId;
    }

    public Wishlist() {
    }

    public Long getUserId() {
        return userId;
    }

    public Long getGiftId() {
        return giftId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setGiftId(Long giftId) {
        this.giftId = giftId;
    }
}
