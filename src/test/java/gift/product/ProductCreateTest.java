package gift.product;

import gift.AbstractControllerTest;
import gift.dto.ProductCreateRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

public class ProductCreateTest extends AbstractControllerTest {

    private final HeaderDescriptor[] PRODUCT_CREATE_REQUEST_HEADERS = {
            headerWithName("X-User-Role").description("사용자 역할 헤더").optional()
    };

    private final FieldDescriptor[] PRODUCT_CREATE_REQUEST = {
            fieldWithPath("name").description("제품 이름").type(JsonFieldType.STRING),
            fieldWithPath("price").description("제품 가격").type(JsonFieldType.NUMBER),
            fieldWithPath("imageUrl").description("제품 이미지 URL").type(JsonFieldType.STRING),
            fieldWithPath("isMdApproved").description("카카오 제품 여부").type(JsonFieldType.BOOLEAN).optional()
    };

    private final FieldDescriptor[] PRODUCT_CREATE_RESPONSE = {
            fieldWithPath("id").description("제품 ID").type(JsonFieldType.NUMBER),
            fieldWithPath("name").description("제품 이름").type(JsonFieldType.STRING),
            fieldWithPath("price").description("제품 가격").type(JsonFieldType.NUMBER),
            fieldWithPath("imageUrl").description("제품 이미지 URL").type(JsonFieldType.STRING)
    };

    @Test
    @DisplayName("제품 생성 성공 테스트")
    public void 제품_생성_시_201_반환() {
        ProductCreateRequest request =
                new ProductCreateRequest("새로운 제품", 1000L, "이미지 URL", null);

        RestAssured.given(this.spec)
                .filter(document("상품 생성 성공",
                        requestFields(PRODUCT_CREATE_REQUEST),
                        requestHeaders(PRODUCT_CREATE_REQUEST_HEADERS),
                        responseFields(PRODUCT_CREATE_RESPONSE)
                ))
                .contentType("application/json")
                .body(request)
                .when()
                .post(getBaseUrl() + "/api/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("price", notNullValue())
                .body("imageUrl", notNullValue());
    }

    @Test
    @DisplayName("제품 생성 성공 테스트: MD 권한 시 201 반환")
    public void 제품_생성_카카오_md_권한_시_201_반환() {
        // 카카오 포함 시 MD 권한이 있는 사용자로 제품 생성
        ProductCreateRequest request =
                new ProductCreateRequest("MD 카카오 제품", 1000L, "이미지 URL", null);

        RestAssured.given(this.spec)
                .filter(document("MD 권한 시 제품 생성 성공",
                        requestFields(PRODUCT_CREATE_REQUEST),
                        requestHeaders(PRODUCT_CREATE_REQUEST_HEADERS),
                        responseFields(PRODUCT_CREATE_RESPONSE)
                    ))
                .header("X-User-Role", "ROLE_MD")
                .contentType("application/json")
                .body(request)
                .when()
                .post(getBaseUrl() + "/api/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("price", notNullValue())
                .body("imageUrl", notNullValue());
    }

    @Test
    @DisplayName("제품 생성 실패 테스트 : 특정 필드 누락")
    public void 제품_생성_특정_필드_누락_시_400_반환() {
        String url = getBaseUrl() + "/api/products";
        // 제품 이름 누락
        ProductCreateRequest request
                = new ProductCreateRequest(null, 1000L, "이미지 URL", null);

        RestAssured.given(this.spec)
                .filter(document("상품 생성 실패 - 필드 누락",
                        requestFields(
                                fieldWithPath("name").description("제품 이름").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("price").description("제품 가격").type(JsonFieldType.NUMBER),
                                fieldWithPath("imageUrl").description("제품 이미지 URL").type(JsonFieldType.STRING),
                                fieldWithPath("isMdApproved").description("카카오 제품 여부").type(JsonFieldType.BOOLEAN).optional()
                        ),
                        responseFields(ERROR_MESSAGE_FIELDS)
                ))
                .contentType("application/json")
                .body(request)
                .when()
                .post(url)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("제품 생성 실패 테스트 : 제품 이름 유효성 검사 실패")
    public void 제품_생성_제품이름_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products";
         //유효하지않은 특수 문자 포함된 15자 이상의 제품 이름
        ProductCreateRequest request = new ProductCreateRequest(
                "<><><><><><><><><><><><><><><><>",
                1000L, "이미지 URL", null);

        RestAssured.given(this.spec)
                .filter(document("상품 생성 실패 - 제품 이름 유효성 검사 실패",
                        requestFields(PRODUCT_CREATE_REQUEST),
                        requestHeaders(PRODUCT_CREATE_REQUEST_HEADERS),
                        responseFields(ERROR_MESSAGE_FIELDS)
                ))
                .contentType("application/json")
                .body(request)
                .when()
                .post(url)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("제품 생성 실패 테스트 : 가격 유효성 검사 실패")
    public void 제품_생성_가격_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products";
        //유효하지 않은 데이터
        ProductCreateRequest request =
                new ProductCreateRequest("", -1000L, "이미지 URL", null);
        RestAssured.given(this.spec)
                .filter(document("상품 생성 실패 - 가격 유효성 검사 실패",
                        requestFields(PRODUCT_CREATE_REQUEST),
                        requestHeaders(PRODUCT_CREATE_REQUEST_HEADERS),
                        responseFields(ERROR_MESSAGE_FIELDS)
                ))
                .contentType("application/json")
                .body(request)
                .when()
                .post(url)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("제품 생성 카카오 유효성 검사 실패 시 400 반환")
    public void 제품_생성_카카오_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products";
        // 카카오 제품이지만 MD 권한이 없는 사용자로 제품 생성
        ProductCreateRequest request =
                new ProductCreateRequest("카카오 제품", 1000L, "이미지 URL", null);

        RestAssured.given(this.spec)
                .filter(document("카카오 제품 생성 실패 - MD 권한 없음",
                        requestFields(PRODUCT_CREATE_REQUEST),
                        requestHeaders(PRODUCT_CREATE_REQUEST_HEADERS),
                        responseFields(ERROR_MESSAGE_FIELDS)
                ))
                .contentType("application/json")
                .body(request)
                .when()
                .post(url)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", notNullValue());
    }

}
