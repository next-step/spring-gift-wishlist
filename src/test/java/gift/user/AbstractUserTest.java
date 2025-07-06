package gift.user;


import gift.AbstractControllerTest;
import gift.dto.auth.LoginRequest;
import gift.dto.auth.TokenResponse;
import gift.dto.user.UserAdminResponse;
import gift.dto.user.UserCreateRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUserTest extends AbstractControllerTest {

    protected List<UserAdminResponse> testUsers;
    protected String testAdminToken;
    protected String testUserToken;

    private UserAdminResponse getAdminResponse(UserCreateRequest request) {
        return RestAssured.given()
                .contentType("application/json")
                .header(AUTH_HEADER_KEY, this.adminToken) // 관리자 토큰 사용
                .body(request)
                .post(getBaseUrl() + "/api/users")
                .then()
                .statusCode(201)
                .extract()
                .as(UserAdminResponse.class);
    }

    private String getToken(UserCreateRequest request) {
        TokenResponse response =  RestAssured.given()
                .contentType("application/json")
                .body(new LoginRequest(request.email(), request.password()))
                .post(getBaseUrl() + "/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .as(TokenResponse.class);
        return "Bearer " + response.token();
    }


    @BeforeEach
    public void setUp(RestDocumentationContextProvider provider) {
        super.setUp(provider);
        this.testUsers = new ArrayList<>();

        UserCreateRequest userRequest = new UserCreateRequest(
                "user1@example.com",
                "password123!",
                "ROLE_USER"
        );
        UserAdminResponse userResponse = getAdminResponse(userRequest);
        this.testUsers.add(userResponse);
        this.testUserToken = getToken(userRequest);

        UserCreateRequest adminRequest = new UserCreateRequest(
                "user2@example.com",
                "password123!",
                "ROLE_ADMIN"
        );
        UserAdminResponse adminResponse = getAdminResponse(adminRequest);
        this.testUsers.add(adminResponse);
        this.testAdminToken = getToken(adminRequest);
    }

    @AfterEach
    public void tearDown() {
        if (this.testUsers == null || this.testUsers.isEmpty()) {
            return;
        }
        for (UserAdminResponse user : this.testUsers) {
            RestAssured.given()
                    .header(AUTH_HEADER_KEY, this.adminToken)
                    .when()
                    .delete(getBaseUrl() + "/api/users/" + user.id())
                    .then()
                    .statusCode(204);
        }
        this.testUsers.clear();
    }

    public String getRequestUrl() {
        return getBaseUrl() + "/api/users";
    }

}
