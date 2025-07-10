package gift.domain;

import java.time.LocalDateTime;

public class WishList {
    private Long id;
    private Long memberId;
    private Long productId;
    private LocalDateTime createdAt;

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public WishList(Long memberId, Long productId) {
        this.memberId = memberId;
        this.productId = productId;
    }
}
