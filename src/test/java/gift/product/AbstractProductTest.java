package gift.product;

import gift.AbstractControllerTest;
import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductDefaultResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractProductTest extends AbstractControllerTest {

    protected List<ProductDefaultResponse> testProductIds;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider provider) {
        super.setUp(provider);
        ProductCreateRequest request =
                new ProductCreateRequest("테스트 제품", 1000L, "이미지 URL");

        this.testProductIds = new ArrayList<>();
        ProductDefaultResponse response = RestAssured.given()
                .header(AUTH_HEADER_KEY, this.adminToken)
                .contentType("application/json")
                .body(request)
                .post(getRequestUrl())
                .then()
                .statusCode(201)
                .extract()
                .as(ProductDefaultResponse.class);
        this.testProductIds.add(response);
    }

    @AfterEach
    public void tearDown() {
        if (this.testProductIds == null || this.testProductIds.isEmpty()) {
            return;
        }

        for (ProductDefaultResponse response : this.testProductIds) {
            RestAssured.given()
                    .header(AUTH_HEADER_KEY, this.adminToken)
                    .when()
                    .delete(getBaseUrl() + "/api/products/" + response.id())
                    .then()
                    .statusCode(204);
        }
        this.testProductIds.clear();
    }

    public String getRequestUrl() {
        return getBaseUrl() + "/api/products";
    }
}
