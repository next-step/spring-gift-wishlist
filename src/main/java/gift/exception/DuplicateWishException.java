package gift.exception;

public class DuplicateWishException extends RuntimeException {
    public DuplicateWishException(Long memberId, Long productId) {
        super(String.format("회원 %d의 위시리스트에 상품 %d가 이미 존재합니다.", memberId, productId));
    }
}