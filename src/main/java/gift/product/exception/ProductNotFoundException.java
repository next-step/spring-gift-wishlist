package gift.product.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("해당 상품을 찾을 수 없습니다. id=" + id);
    }
}