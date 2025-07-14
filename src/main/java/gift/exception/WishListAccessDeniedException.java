package gift.exception;

public class WishListAccessDeniedException extends RuntimeException {
    public WishListAccessDeniedException(String message) {
        super(message);
    }

    public WishListAccessDeniedException() {
        super("위시리스트에 접근할 권한이 없습니다");
    }
}
