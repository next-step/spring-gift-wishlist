package gift.exception;

public class ForbiddenException extends RuntimeException {
  public ForbiddenException(String message) {
    super("인가 실패 : " + message);
  }
}
