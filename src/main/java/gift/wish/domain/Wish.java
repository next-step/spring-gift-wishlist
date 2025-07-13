package gift.wish.domain;

import gift.wish.exception.WishOwnerException;

public class Wish {
    private Long id;
    private Long memberId;
    private Long productId;
    private Integer quantity;

    public Wish(Long id, Long memberId, Long productId, Integer quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public void validateOwner(Long memberId) {
        if(!this.memberId.equals(memberId)) {
            throw new WishOwnerException("해당 위시 항목을 삭제할 권한이 없습니다.");
        }
    }
}
