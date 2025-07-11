package gift.entity;

import java.time.LocalDateTime;

public class Wish {
    private Long id;
    private Long memberId;
    private Long productId;
    private LocalDateTime createdAt;

    public Wish(Long memberId, Long productId) {
        this.memberId = memberId;
        this.productId = productId;
        this.createdAt = LocalDateTime.now();
    }

    public Wish(Long id, Long memberId, Long productId, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getMemberId() { return memberId; }
    public Long getProductId() { return productId; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public Wish withId(Long id) {
        return new Wish(id, this.memberId, this.productId, this.createdAt);
    }
}