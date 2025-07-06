package gift.user;

import gift.dto.user.UserAdminResponse;
import gift.dto.user.UserUpdateRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

public class UserUpdateTest extends AbstractUserTest {

    private static final FieldDescriptor[] ADMIN_UPDATE_REQUEST = {
            fieldWithPath("email").description("사용자 이메일").type(JsonFieldType.STRING).optional(),
            fieldWithPath("password").description("사용자 비밀번호").type(JsonFieldType.STRING).optional(),
            fieldWithPath("roles").description("사용자 역할 (기본값: ROLE_USER)").type(JsonFieldType.ARRAY).optional()
    };

    private static final FieldDescriptor[] ADMIN_UPDATE_RESPONSE = {
            fieldWithPath("id").description("사용자 ID").type(JsonFieldType.NUMBER),
            fieldWithPath("email").description("사용자 이메일"),
            fieldWithPath("password").description("인코딩된 사용자 비밀번호"),
            fieldWithPath("roles").description("사용자 역할 목록")
    };

    private static final FieldDescriptor[] USER_UPDATE_RESPONSE = {
            fieldWithPath("id").description("사용자 ID").type(JsonFieldType.NUMBER),
            fieldWithPath("email").description("사용자 이메일").type(JsonFieldType.STRING)
    };

    @Test
    @DisplayName("사용자 수정 성공 테스트 - 관리자 권한")
    void update_User_Admin_Success() {
        String url = getRequestUrl() + "/{id}";
        UserAdminResponse testUser = this.testUsers.stream().findFirst().orElseThrow(
                () -> new IllegalStateException("테스트용 사용자가 존재하지 않습니다.")
        );
        UserUpdateRequest request = new UserUpdateRequest(
                "updated@test.com", "updated1234!", List.of("ROLE_USER"));
        RestAssured.given(this.spec)
                .filter(document("관리자 권한으로 사용자 수정 성공",
                        requestFields(ADMIN_UPDATE_REQUEST),
                        requestHeaders(AUTHENTICATE_HEADERS),
                        pathParameters(parameterWithName("id").description("수정할 사용자 ID")),
                        responseFields(ADMIN_UPDATE_RESPONSE)))
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.testAdminToken) // 관리자 권한으로 요청
                .body(request)
                .when()
                .put(url, testUser.id()) // 존재하는 사용자 ID로 변경
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("email", notNullValue())
                .body("password", notNullValue())
                .body("roles", notNullValue());
    }

    @Test
    @DisplayName("사용자 수정 성공 테스트 - 관리자 권한, 특정 필드 누락")
    void update_user_Admin_Success_With_Partial_Request() {
        String url = getRequestUrl() + "/{id}";
        UserAdminResponse testUser = this.testUsers.stream().findFirst().orElseThrow(
                () -> new IllegalStateException("테스트용 사용자가 존재하지 않습니다.")
        );
        UserUpdateRequest request = new UserUpdateRequest(null, "updated1234!", null);
        RestAssured.given(this.spec)
                .filter(document("관리자 권한으로 사용자 수정 성공 - 특정 필드 누락",
                        requestFields(ADMIN_UPDATE_REQUEST),
                        requestHeaders(AUTHENTICATE_HEADERS),
                        pathParameters(parameterWithName("id").description("수정할 사용자 ID")),
                        responseFields(ADMIN_UPDATE_RESPONSE)))
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.testAdminToken) // 관리자 권한으로 요청
                .body(request)
                .when()
                .put(url, testUser.id()) // 존재하는 사용자 ID로 변경
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("email", notNullValue())
                .body("password", notNullValue())
                .body("roles", notNullValue());
    }

    @Test
    @DisplayName("사용자 수정 성공 테스트 - me 일반 사용자 권한")
    void update_User_Me_Success() {
        String url = getRequestUrl() + "/me";
        UserUpdateRequest request = new UserUpdateRequest(null, "updated1234!", null);
        RestAssured.given(this.spec)
                .filter(document("일반 사용자 권한으로 사용자 수정 성공",
                        requestFields(ADMIN_UPDATE_REQUEST),
                        requestHeaders(AUTHENTICATE_HEADERS),
                        responseFields(USER_UPDATE_RESPONSE)))
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.testUserToken) // 일반 사용자 권한으로 요청
                .body(request)
                .when()
                .put(url) // me 엔드포인트로 요청
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("email", notNullValue());
    }

    @Test
    @DisplayName("사용자 수정 실패 테스트 - 일반 사용자 권한")
    void update_User_Fail_Not_Admin() {
        String url = getRequestUrl() + "/{id}";
        UserAdminResponse testUser = this.testUsers.stream().findFirst().orElseThrow(
                () -> new IllegalStateException("테스트용 사용자가 존재하지 않습니다.")
        );
        UserUpdateRequest request = new UserUpdateRequest(
                "updated@test.com", "updated1234!", List.of("ROLE_USER"));
        RestAssured.given(this.spec)
                .filter(document("일반 사용자 권한으로 사용자 수정 실패",
                        requestFields(ADMIN_UPDATE_REQUEST),
                        requestHeaders(AUTHENTICATE_HEADERS),
                        pathParameters(parameterWithName("id").description("수정할 사용자 ID")),
                        responseFields(ERROR_MESSAGE_FIELDS)))
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.testUserToken) // 일반 사용자 권한으로 요청
                .body(request)
                .when()
                .put(url, testUser.id()) // 존재하는 사용자 ID로 변경
                .then()
                .statusCode(403); // 권한 없음
    }
}
