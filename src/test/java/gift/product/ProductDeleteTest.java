package gift.product;

import gift.AbstractControllerTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

public class ProductDeleteTest extends AbstractControllerTest {

    @Test
    @DisplayName("제품 삭제 성공 테스트")
    public void 제품_삭제_시_204_반환() {
        String url = getBaseUrl() + "/api/products/{id}";
        RestAssured.given(this.spec)
                .filter(document("상품 삭제 성공",
                        pathParameters(
                                parameterWithName("id").description("삭제할 제품 ID")
                        )
                ))
                .when()
                .delete(url, 10) // 존재하는 제품 ID로 변경
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("제품 삭제 실패 테스트: 존재하지 않는 제품 ID")
    public void 존재하지_않는_제품_삭제_시_404_반환() {
        String url = getBaseUrl() + "/api/products/{id}";
        RestAssured.given(this.spec)
                .filter(document("상품 삭제 실패 - 존재하지 않는 제품 ID",
                    pathParameters(
                        parameterWithName("id").description("삭제할 제품 ID")
                    ),
                    responseFields(ERROR_MESSAGE_FIELDS)
                ))
                .when()
                .delete(url, 9999) // 존재하지 않는 제품 ID
                .then()
                .statusCode(404);
    }
}
