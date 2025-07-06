package gift.auth;

import gift.dto.auth.LoginRequest;
import gift.dto.auth.SignupRequest;
import gift.dto.user.UserCreateRequest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

public class AuthTest extends AbstractAuthTest {

    private static final FieldDescriptor[] LOGIN_REQUEST_FIELDS = {
        fieldWithPath("email").description("사용자 이메일").type(JsonFieldType.STRING),
        fieldWithPath("password").description("사용자 비밀번호").type(JsonFieldType.STRING)
    };

    private static final FieldDescriptor[] SIGNUP_REQUEST_FIELDS = {
        fieldWithPath("email").description("사용자 이메일").type(JsonFieldType.STRING),
        fieldWithPath("password").description("사용자 비밀번호").type(JsonFieldType.STRING),
        fieldWithPath("passwordConfirm").description("비밀번호 확인").type(JsonFieldType.STRING),
    };

    private static final FieldDescriptor[] AUTH_RESPONSE_FIELDS = {
        fieldWithPath("token").description("인증 토큰").type(JsonFieldType.STRING)
    };

    @Test
    @DisplayName("로그인 성공 테스트")
    public void testLoginSuccess() {
        UserCreateRequest req = this.testUsers
                .keySet()
                .stream()
                .findFirst()
                .orElseThrow();

        // 로그인 요청
        RestAssured.given(this.spec)
                .filter(document("로그인 성공",
                        requestFields(LOGIN_REQUEST_FIELDS),
                        responseFields(AUTH_RESPONSE_FIELDS)))
                .contentType("application/json")
                .body(new LoginRequest(req.email(), req.password()))
                .when()
                .post(getBaseUrl() + "/api/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 잘못된 이메일 또는 비밀번호")
    public void testLoginFailure() {
        RestAssured.given()
                .contentType("application/json")
                .body(new LoginRequest("notexist@test.com", "password123!"))
                .when()
                .post(getBaseUrl() + "/api/auth/login")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void testSignupSuccess() {
        SignupRequest request = new SignupRequest(
                "newuser@test.com", "password123!", "password123!");

        RestAssured.given(this.spec)
                .filter(document("회원가입 성공",
                        requestFields(SIGNUP_REQUEST_FIELDS),
                        responseFields(AUTH_RESPONSE_FIELDS)))
                .contentType("application/json")
                .body(request)
                .when()
                .post(getBaseUrl() + "/api/auth/signup")
                .then()
                .statusCode(201)
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 비밀번호 불일치")
    public void testSignupFailure() {
        SignupRequest request = new SignupRequest(
                "newuser@test.com", "password123!", "differentPassword!");
        RestAssured.given(this.spec)
                .filter(document("회원가입 실패 - 비밀번호 불일치",
                        requestFields(SIGNUP_REQUEST_FIELDS),
                        responseFields(ERROR_MESSAGE_FIELDS)))
                .contentType("application/json")
                .body(request)
                .when()
                .post(getBaseUrl() + "/api/auth/signup")
                .then()
                .statusCode(400)
                .body("title", notNullValue())
                .body("detail", notNullValue())
                .body("validationErrors", Matchers.hasSize(1));
    }

}
