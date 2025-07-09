package gift.global.exception;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    NOT_EXISTS(HttpStatus.NOT_FOUND, "객체를 찾을 수 없습니다."),
    //NOT_EXISTS_PRODUCT(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    INVALID_PRODUCT_REQUEST(HttpStatus.BAD_REQUEST, "상품 이름, 가격, 이미지 주소는 필수 정보입니다."),
    INVALID_PRODUCT_PRICE(HttpStatus.BAD_REQUEST, "상품 가격은 0원 이상이어야 합니다."),
    INVALID_PRODUCT_UPDATE_REQUEST(HttpStatus.BAD_REQUEST, "수정 요청 객체를 찾을 수 없습니다."),
    INVALID_KAKAO_NAME(HttpStatus.BAD_REQUEST, "'카카오'가 들어간 상품명은 관리자에게 문의해 주세요."),
    INVALID_FORM_REQUEST(HttpStatus.BAD_REQUEST, "허용되지 않은 값을 입력하셨습니다."),
    INVALID_TOKEN_REQUEST(HttpStatus.UNAUTHORIZED, "잘못된 형식의 토큰입니다."),
    //NOT_EXISTS_MEMBER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INCORRECT_LOGIN_INFO(HttpStatus.FORBIDDEN, "이메일, 비밀번호 조합이 잘못되었습니다."),
    DUPLICATE_EMAIL(HttpStatus.FORBIDDEN, "이미 사용 중인 이메일입니다."),
    OTHERS(HttpStatus.INTERNAL_SERVER_ERROR, "기타 오류입니다.");


    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
