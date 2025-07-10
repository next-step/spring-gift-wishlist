package gift.domain;

import java.time.LocalDateTime;

public class Wish {

    private final Long id;                    // 찜 항목 고유 ID (wishId)
    private final Long memberId;              // 사용자 ID (FK to member)
    private final Long productId;             // 상품 ID (FK to product)
    private final LocalDateTime createdDate;  // 찜한 시간 (정렬 기준)

    private Wish(Long id, Long memberId, Long productId, LocalDateTime createdDate) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.createdDate = createdDate;
    }

    public static Wish of(Long id, Long memberId, Long productId, LocalDateTime createdDate) {
        return new Wish(id, memberId, productId, createdDate);
    }

    public Long getId() { return id; }
    public Long getMemberId() { return memberId; }
    public Long getProductId() { return productId; }
    public LocalDateTime getCreatedDate() { return createdDate; }
}
