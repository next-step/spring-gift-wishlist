package gift.product;

import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductDefaultResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

public class ProductCreateTest extends AbstractProductTest {

    private final FieldDescriptor[] PRODUCT_CREATE_REQUEST = {
            fieldWithPath("name").description("제품 이름('카카오'가 포함될 시 MD 이상의 권한 필요)").type(JsonFieldType.STRING),
            fieldWithPath("price").description("제품 가격").type(JsonFieldType.NUMBER),
            fieldWithPath("imageUrl").description("제품 이미지 URL").type(JsonFieldType.STRING),
    };

    private final FieldDescriptor[] PRODUCT_CREATE_BAD_REQUEST = {
            fieldWithPath("name").description("제품 이름('카카오'가 포함될 시 MD 이상의 권한 필요)").type(JsonFieldType.STRING).optional(),
            fieldWithPath("price").description("제품 가격").type(JsonFieldType.NUMBER).optional(),
            fieldWithPath("imageUrl").description("제품 이미지 URL").type(JsonFieldType.STRING).optional()
    };

    private final FieldDescriptor[] PRODUCT_CREATE_RESPONSE = {
            fieldWithPath("id").description("제품 ID").type(JsonFieldType.NUMBER),
            fieldWithPath("name").description("제품 이름").type(JsonFieldType.STRING),
            fieldWithPath("price").description("제품 가격").type(JsonFieldType.NUMBER),
            fieldWithPath("imageUrl").description("제품 이미지 URL").type(JsonFieldType.STRING)
    };

    @Test
    @DisplayName("제품 생성 성공 테스트")
    public void Product_Create_Success() {
        ProductCreateRequest request =
                new ProductCreateRequest("새로운 제품", 1000L, "이미지 URL");
        ProductDefaultResponse response = RestAssured.given(this.spec)
                .filter(document("상품 생성 성공",
                        requestFields(PRODUCT_CREATE_REQUEST),
                        requestHeaders(AUTHENTICATE_HEADERS),
                        responseFields(PRODUCT_CREATE_RESPONSE)
                ))
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.adminToken) // 관리자 권한으로 요청
                .body(request)
                .when()
                .post(getRequestUrl())
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("price", notNullValue())
                .body("imageUrl", notNullValue())
                .extract()
                .as(ProductDefaultResponse.class);
        this.testProductIds.add(response);
    }

    @Test
    @DisplayName("제품 생성 실패 - 필수 필드 누락(400 Bad Request) 테스트")
    public void Product_Create_Failure_MissingFields() {
        ProductCreateRequest request = new ProductCreateRequest(null, null, null);


        RestAssured.given(this.spec)
                .filter(document("상품 생성 실패 - 필수 필드 누락",
                        requestFields(PRODUCT_CREATE_BAD_REQUEST),
                        requestHeaders(AUTHENTICATE_HEADERS),
                        responseFields(ERROR_MESSAGE_FIELDS)
                ))
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.adminToken) // 관리자 권한으로 요청
                .body(request)
                .when()
                .post(getRequestUrl())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", notNullValue())
                .body("detail", notNullValue())
                .body("validationErrors", notNullValue())
                .body("validationErrors", hasSize(4));
    }

    @Test
    @DisplayName("제품 생성 실패 - 권한 없음(403 Forbidden) 테스트")
    public void Product_Create_Failure_Unauthorized() {
        ProductCreateRequest request = new ProductCreateRequest("제품 이름", 1000L, "이미지 URL");

        RestAssured.given(this.spec)
                .filter(document("상품 생성 실패 - 권한 없음",
                        requestFields(PRODUCT_CREATE_REQUEST),
                        requestHeaders(AUTHENTICATE_HEADERS),
                        responseFields(ERROR_MESSAGE_FIELDS)
                ))
                .contentType("application/json")
                .body(request)
                .when()
                .post(getRequestUrl())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("title", notNullValue())
                .body("detail" , notNullValue());
    }
}
