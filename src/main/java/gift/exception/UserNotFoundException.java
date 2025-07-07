package gift.exception;

// 유저 못 찾았을 때 반환하는 예외
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("No user found with id: " + id);
    }

    public UserNotFoundException() {
        super("No user found");
    }
}
