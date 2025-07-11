package gift.exception;

public class WishListItemNotFoundException extends RuntimeException {
  public WishListItemNotFoundException(Long memberId, Long productId) {
    super("memberId = " + memberId + ", productId = " + productId + " 에 해당하는 위시리스트 항목이 존재하지 않습니다.");
  }
}
