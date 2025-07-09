package gift.wishlist;

public class Wishlist {

    private Long id;
    private Long memberId;
    private Long itemId;

    public Wishlist(Long id, Long memberId, Long itemId) {
        this.id = id;
        this.memberId = memberId;
        this.itemId = itemId;
    }

    public Wishlist(Long memberId, Long itemId) {
        this.memberId = memberId;
        this.itemId = itemId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}

