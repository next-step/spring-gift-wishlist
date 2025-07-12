package gift.exception;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(Long id) {
    super("해당 ID = " + id + " 의 상품이 존재하지 않습니다.");
  }
}
