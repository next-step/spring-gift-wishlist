package gift.entity;

import java.time.LocalDateTime;

public class Wish {
    private final Long id;
    private final Long memberId;
    private final Long productId;
    private final LocalDateTime createdAt;

    public Wish(Long id, Long memberId, Long productId, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.createdAt = createdAt;
    }

    public Wish(Long memberId, Long productId) {
        this.id = null;
        this.memberId = memberId;
        this.productId = productId;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getMemberId() { return memberId; }
    public Long getProductId() { return productId; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public Wish withId(Long id) {
        return new Wish(id, this.memberId, this.productId, this.createdAt);
    }
}