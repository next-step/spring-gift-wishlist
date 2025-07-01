package gift.exception;

public class KakaoNameNotAllowedException extends RuntimeException {
    public KakaoNameNotAllowedException() {
        super("상품명에 '카카오'가 포함된 경우, 담당 MD와 협의 후 등록 가능합니다.");
    }
}
