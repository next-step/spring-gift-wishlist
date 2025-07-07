package gift.exception;

public class ProductNotFoundExection extends RuntimeException {

    public ProductNotFoundExection(Long id) {
        super("상품을 찾을 수 없습니다 " + id);
    }
}
