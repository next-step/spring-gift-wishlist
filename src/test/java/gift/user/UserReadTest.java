package gift.user;

import gift.dto.user.UserAdminResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

public class UserReadTest extends  AbstractUserTest{

    public static final FieldDescriptor[] MULTIPLE_ADMIN_READ_RESPONSE = {
            fieldWithPath("page").description("현재 페이지 번호").type(JsonFieldType.NUMBER),
            fieldWithPath("size").description("페이지 크기").type(JsonFieldType.NUMBER),
            fieldWithPath("totalElements").description("전체 요소 수").type(JsonFieldType.NUMBER),
            fieldWithPath("totalPages").description("전체 페이지 수").type(JsonFieldType.NUMBER),
            fieldWithPath("contents[]").description("사용자 목록").type(JsonFieldType.ARRAY),
            fieldWithPath("contents[].id").description("사용자 ID").type(JsonFieldType.NUMBER).optional(),
            fieldWithPath("contents[].email").description("사용자 이메일").type(JsonFieldType.STRING).optional(),
            fieldWithPath("contents[].password").description("인코딩된 사용자 비밀번호").type(JsonFieldType.STRING).optional(),
            fieldWithPath("contents[].roles").description("사용자 역할 목록").type(JsonFieldType.ARRAY).optional()
    };

    public static final ParameterDescriptor[] PAGE_PARAMETERS = {
            parameterWithName("page").description("페이지 번호(0부터 시작)").optional(),
            parameterWithName("size").description("페이지 크기(기본값: 5)").optional()
    };

    public static final FieldDescriptor[] SINGLE_ADMIN_READ_RESPONSE = {
            fieldWithPath("id").description("사용자 ID").type(JsonFieldType.NUMBER),
            fieldWithPath("email").description("사용자 이메일").type(JsonFieldType.STRING),
            fieldWithPath("password").description("인코딩된 사용자 비밀번호").type(JsonFieldType.STRING),
            fieldWithPath("roles").description("사용자 역할 목록").type(JsonFieldType.ARRAY)
    };

    public static final FieldDescriptor[] SINGLE_USER_READ_RESPONSE = {
            fieldWithPath("id").description("사용자 ID").type(JsonFieldType.NUMBER),
            fieldWithPath("email").description("사용자 이메일").type(JsonFieldType.STRING),
    };



    @Test
    @DisplayName("다건 사용자 조회 성공 테스트: 관리자 권한으로 요청")
    public void find_All_Users_Success() {
        String url = getRequestUrl();
        RestAssured.given(this.spec)
                .filter(document("사용자 전체 조회 성공",
                        responseFields(MULTIPLE_ADMIN_READ_RESPONSE),
                        requestHeaders(AUTHENTICATE_HEADERS),
                        queryParameters(PAGE_PARAMETERS))
                )
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.testAdminToken) // 관리자 권한으로 요청
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .body("page", notNullValue())
                .body("size", notNullValue())
                .body("totalElements", notNullValue())
                .body("totalPages", notNullValue())
                .body("contents", notNullValue())
                .body("contents[0].id", notNullValue())
                .body("contents[0].email", notNullValue())
                .body("contents[0].password", notNullValue())
                .body("contents[0].roles", notNullValue());
    }

    @Test
    @DisplayName("다건 사용자 조회 실패 테스트: 관리자 권한 없이 요청(403 Forbidden)")
    public void find_All_Users_Failure_NoAuth() {
        String url = getRequestUrl();
        RestAssured.given()
                .contentType("application/json")
                .when()
                .get(url)
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("다건 사용자 실패 테스트: page 파라미터가 음수인 경우 400 Bad Request")
    public void find_All_Users_Failure_Negative_Page_Request() {
        String url = getRequestUrl();
        RestAssured.given()
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.testAdminToken) // 관리자 권한으로 요청
                .when()
                .get(url + "?page=-1&size=5")
                .then()
                .statusCode(400);
    }




    @Test
    @DisplayName("단건 사용자 조회 성공 테스트: 관리자 권한으로 요청")
    public void find_User_By_Id_Success() {
        String url = getRequestUrl() + "/{id}";
        Optional<UserAdminResponse> adminResponse = this.testUsers.stream()
                .filter(user -> user.roles().contains("ROLE_ADMIN"))
                .findFirst();
        assertTrue(adminResponse.isPresent(), "관리자 권한을 가진 사용자가 존재해야 합니다.");
        Long userId = adminResponse.get().id();

        RestAssured.given(this.spec)
                .filter(document("사용자 ID로 조회 성공",
                        responseFields(SINGLE_ADMIN_READ_RESPONSE),
                        requestHeaders(AUTHENTICATE_HEADERS),
                        pathParameters(
                                parameterWithName("id").description("조회할 사용자 ID")
                        )
                ))
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.adminToken) // 관리자 권한으로 요청
                .when()
                .get(url, userId) // 존재하는 사용자 ID로 변경
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("email", notNullValue())
                .body("password", notNullValue())
                .body("roles", notNullValue());
    }

    @Test
    @DisplayName("단건 사용자 조회 실패 테스트: 존재하지 않는 사용자 ID")
    public void find_User_By_Id_Failure_NonExistentId() {
        String url = getRequestUrl() + "/{id}";
        RestAssured.given()
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.adminToken) // 관리자 권한으로 요청
                .when()
                .get(url, 9999) // 존재하지 않는 사용자 ID로 변경
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("단건 사용자 조회 실패 테스트: 관리자 권한 없이 요청(403 Forbidden)")
    public void find_User_By_Id_Failure_NoAuth() {
        String url = getRequestUrl() + "/{id}";
        Optional<UserAdminResponse> userResponse = this.testUsers.stream()
                .filter(user -> user.roles().contains("ROLE_USER"))
                .findFirst();
        assertTrue(userResponse.isPresent(), "일반 사용자 권한을 가진 사용자가 존재해야 합니다.");
        Long userId = userResponse.get().id();
        RestAssured.given()
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.testUserToken) // 일반 사용자 권한으로 요청
                .when()
                .get(url, userId) // 일반 사용자 권한으로 요청
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("단건 사용자 조회 성공 테스트: 일반 사용자 권한으로 요청")
    public void find_User_By_Id_Success_As_User() {
        String url = getRequestUrl() + "/me";
        Optional<UserAdminResponse> userResponse = this.testUsers.stream()
                .filter(user -> user.roles().contains("ROLE_USER"))
                .findFirst();
        assertTrue(userResponse.isPresent(), "일반 사용자 권한을 가진 사용자가 존재해야 합니다.");
        UserAdminResponse user = userResponse.get();
        RestAssured.given(this.spec)
                .filter(document("사용자 단건 조회 성공 - 일반 사용자 권한",
                        responseFields(SINGLE_USER_READ_RESPONSE),
                        requestHeaders(AUTHENTICATE_HEADERS)
                        ))
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.testUserToken) // 일반 사용자 권한으로 요청
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("id", equalTo(userResponse.get().id().intValue()))
                .body("email", notNullValue())
                .body("email", equalTo(userResponse.get().email()));
    }
}
