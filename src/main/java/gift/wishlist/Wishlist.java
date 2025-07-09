package gift.wishlist;

import java.time.LocalDateTime;

public class Wishlist {

    private Long id;
    private Long memberId;
    private Long itemId;
    private LocalDateTime createdAt;

    public Wishlist(Long id, Long memberId, Long itemId, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.itemId = itemId;
        this.createdAt = createdAt;
    }

    public Wishlist(Long memberId, Long itemId, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.itemId = itemId;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

