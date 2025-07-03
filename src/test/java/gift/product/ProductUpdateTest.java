package gift.product;

import gift.AbstractControllerTest;
import gift.dto.ProductUpdateRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

public class ProductUpdateTest extends AbstractControllerTest {

    private static final ParameterDescriptor[] PRODUCT_UPDATE_PATH_PARAMETERS = {
            parameterWithName("id").description("수정할 제품 ID")
    };

    private static final HeaderDescriptor[] PRODUCT_UPDATE_REQUEST_HEADERS = {
            headerWithName("X-User-Role").description("사용자 역할 헤더").optional()
    };

    private static final FieldDescriptor[] PRODUCT_UPDATE_REQUEST = {
            fieldWithPath("name").description("수정된 제품 이름").type(JsonFieldType.STRING).optional(),
            fieldWithPath("price").description("수정된 제품 가격").type(JsonFieldType.NUMBER).optional(),
            fieldWithPath("imageUrl").description("수정된 제품 이미지 URL").type(JsonFieldType.STRING).optional()
    };

    private static final FieldDescriptor[] PRODUCT_UPDATE_RESPONSE = {
            fieldWithPath("id").description("제품 ID").type(JsonFieldType.NUMBER),
            fieldWithPath("name").description("제품 이름").type(JsonFieldType.STRING),
            fieldWithPath("price").description("제품 가격").type(JsonFieldType.NUMBER),
            fieldWithPath("imageUrl").description("제품 이미지 URL").type(JsonFieldType.STRING)
    };

    @Test
    @DisplayName("제품 수정 성공 테스트")
    public void 제품_수정_시_200_반환() {
        String url = getBaseUrl() + "/api/products/{id}";
        ProductUpdateRequest request = new ProductUpdateRequest("수정된 제품", 1500L, "수정된 이미지 URL");
        RestAssured.given(this.spec)
                .filter(document("상품 수정 성공",
                        pathParameters(PRODUCT_UPDATE_PATH_PARAMETERS),
                        requestHeaders(PRODUCT_UPDATE_REQUEST_HEADERS),
                        requestFields(PRODUCT_UPDATE_REQUEST),
                        responseFields(PRODUCT_UPDATE_RESPONSE)))
                .contentType("application/json")
                .body(request)
                .when()
                .put(url, 1) // 존재하는 제품 ID로 변경
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("price", notNullValue())
                .body("imageUrl", notNullValue());
    }

    @Test
    @DisplayName("제품 수정 성공 테스트: 특정 필드 누락 가능")
    public void 제품_수정_특정_필드_누락_시_200_반환() {
        String url = getBaseUrl() + "/api/products/{id}"; // 존재하는 제품 ID로 변경
        ProductUpdateRequest request = new ProductUpdateRequest("수정된 제품", null, "수정된 이미지 URL"); // 가격 필드 누락

        RestAssured.given(this.spec)
                .filter(document("상품 수정 성공 - 특정 필드 누락",
                        pathParameters(PRODUCT_UPDATE_PATH_PARAMETERS),
                        requestHeaders(PRODUCT_UPDATE_REQUEST_HEADERS),
                        requestFields(PRODUCT_UPDATE_REQUEST),
                        responseFields(PRODUCT_UPDATE_RESPONSE)))
                .contentType("application/json")
                .body(request)
                .when()
                .put(url, 1) // 존재하는 제품 ID로 변경
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("price", notNullValue())
                .body("imageUrl", notNullValue());
    }

    @Test
    @DisplayName("제품 수정 성공 테스트 : 카카오 MD 권한 시 200 반환")
    public void 제품_수정_카카오_md_권한_시_200_반환() {
        String url = getBaseUrl() + "/api/products/{id}";
        RestAssured.given(this.spec)
                .filter(document("MD 권한 시 제품 수정 성공",
                        pathParameters(PRODUCT_UPDATE_PATH_PARAMETERS),
                        requestHeaders(PRODUCT_UPDATE_REQUEST_HEADERS),
                        requestFields(PRODUCT_UPDATE_REQUEST),
                        responseFields(PRODUCT_UPDATE_RESPONSE)))
                .header("X-User-Role", "ROLE_MD") // MD 권한으로 요청
                .contentType("application/json")
                .body(new ProductUpdateRequest("MD 권한 카카오 수정 제품", 2000L, "수정된 이미지 URL"))
                .when()
                .put(url, 1) // 존재하는 제품 ID로 변경
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("price", notNullValue())
                .body("imageUrl", notNullValue());
    }

    @Test
    @DisplayName("제품 수정 실패 테스트 : 유효성 검사 실패 시 400 반환")
    public void 제품_수정_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products/{id}";
        ProductUpdateRequest request = new ProductUpdateRequest("<><><><><><><><><><><><><>",
                1000L, "이미지 URL"); // 유효하지 않은 데이터
        RestAssured.given(this.spec)
                .filter(document("상품 수정 실패 - 유효성 검사 실패",
                        pathParameters(PRODUCT_UPDATE_PATH_PARAMETERS),
                        requestHeaders(PRODUCT_UPDATE_REQUEST_HEADERS),
                        requestFields(PRODUCT_UPDATE_REQUEST),
                        responseFields(ERROR_MESSAGE_FIELDS)))
                .contentType("application/json")
                .body(request)
                .when()
                .put(url, 1) // 존재하는 제품 ID로 변경
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("제품 수정 실패 테스트 : 카카오 유효성 검사 실패 시 400 반환")
    public void 제품_수정_카카오_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products/{id}";
        ProductUpdateRequest request = new ProductUpdateRequest("카카오 수정 제품", 1000L, "이미지 URL"); // 유효하지 않은 카카오 데이터

        RestAssured.given(this.spec)
                .filter(document("상품 수정 실패 - 카카오 유효성 검사 실패",
                        pathParameters(PRODUCT_UPDATE_PATH_PARAMETERS),
                        requestHeaders(PRODUCT_UPDATE_REQUEST_HEADERS),
                        requestFields(PRODUCT_UPDATE_REQUEST),
                        responseFields(ERROR_MESSAGE_FIELDS)))
                .contentType("application/json")
                .body(request)
                .when()
                .put(url, 1) // 존재하는 제품 ID로 변경
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("제품 패치 성공 테스트")
    public void 제품_패치_특정_필드_누락_시_200_반환() {
        String url = getBaseUrl() + "/api/products/{id}";
        ProductUpdateRequest request =
                new ProductUpdateRequest("패치된 제품", 1500L, "패치된 이미지 URL");

        RestAssured.given(this.spec)
                .filter(document("상품 패치 성공",
                        pathParameters(PRODUCT_UPDATE_PATH_PARAMETERS),
                        requestHeaders(PRODUCT_UPDATE_REQUEST_HEADERS),
                        requestFields(PRODUCT_UPDATE_REQUEST),
                        responseFields(PRODUCT_UPDATE_RESPONSE)))
                .contentType("application/json")
                .body(request)
                .when()
                .patch(url, 1) // 존재하는 제품 ID로 변경
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("price", notNullValue())
                .body("imageUrl", notNullValue());
    }

    @Test
    @DisplayName("제품 패치 성공 테스트: 카카오 MD 권한 시 200 반환")
    public void 제품_패치_카카오_md_권한_시_200_반환() {
        String url = getBaseUrl() + "/api/products/{id}";
        ProductUpdateRequest request =
                new ProductUpdateRequest("패치된 카카오 제품", 1500L, "패치된 이미지 URL");
        RestAssured.given(this.spec)
                .filter(document("MD 권한 시 상품 패치 성공",
                        pathParameters(PRODUCT_UPDATE_PATH_PARAMETERS),
                        requestHeaders(PRODUCT_UPDATE_REQUEST_HEADERS),
                        requestFields(PRODUCT_UPDATE_REQUEST),
                        responseFields(PRODUCT_UPDATE_RESPONSE)))
                .header("X-User-Role", "ROLE_MD") // MD 권한으로 요청
                .contentType("application/json")
                .body(request)
                .when()
                .patch(url, 1) // 존재하는 제품 ID로 변경
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("price", notNullValue())
                .body("imageUrl", notNullValue());
    }

    @Test
    @DisplayName("제품 패치 실패 테스트 : 이름 필드 유효성 검사 실패 시 400 반환")
    public void 제품_패치_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products/{id}";
        ProductUpdateRequest request =
                new ProductUpdateRequest("<><><><><><><><><><><><><>",
                1000L, "이미지 URL"); // 유효하지 않은 데이터

        RestAssured.given(this.spec)
                .filter(document("상품 패치 실패 - 유효성 검사 실패",
                        pathParameters(PRODUCT_UPDATE_PATH_PARAMETERS),
                        requestHeaders(PRODUCT_UPDATE_REQUEST_HEADERS),
                        requestFields(PRODUCT_UPDATE_REQUEST),
                        responseFields(ERROR_MESSAGE_FIELDS)))
                .contentType("application/json")
                .body(request)
                .when()
                .patch(url, 1) // 존재하는 제품 ID로 변경
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("제품 패치 카카오 유효성 검사 실패 시 400 반환")
    public void 제품_패치_카카오_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products/{id}";
        // 유효하지 않은 카카오 데이터
        ProductUpdateRequest request =
                new ProductUpdateRequest("카카오 수정 제품", 1000L, "이미지 URL");

        RestAssured.given(this.spec)
                .filter(document("상품 패치 실패 - 카카오 유효성 검사 실패",
                        pathParameters(PRODUCT_UPDATE_PATH_PARAMETERS),
                        requestHeaders(PRODUCT_UPDATE_REQUEST_HEADERS),
                        requestFields(PRODUCT_UPDATE_REQUEST),
                        responseFields(ERROR_MESSAGE_FIELDS)))
                .contentType("application/json")
                .body(request)
                .when()
                .patch(url, 1) // 존재하는 제품 ID로 변경
                .then()
                .statusCode(400);
    }
}
