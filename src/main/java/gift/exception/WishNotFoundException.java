package gift.exception;

public class WishNotFoundException extends RuntimeException {
    public WishNotFoundException(Long id) {
        super("존재하지 않는 위시입니다. id=" + id);
    }
}