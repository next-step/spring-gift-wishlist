package gift.product;

import gift.AbstractControllerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductDeleteTest extends AbstractControllerTest {

    @Test
    @DisplayName("제품 삭제 시 204 반환")
    public void 제품_삭제_시_204_반환() {
        String url = getBaseUrl() + "/api/products/5"; // 존재하는 제품 ID로 변경
        ResponseEntity<Void> response = restClient.delete()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Void>() {});

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("존재하지 않는 제품 삭제 시 404 반환")
    public void 존재하지_않는_제품_삭제_시_404_반환() {
        String url = getBaseUrl() + "/api/products/9999"; // 존재하지 않는 제품 ID
        try {
            ResponseEntity<Void> response = restClient.delete()
                    .uri(url)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Void>() {});
            Assertions.fail("예외가 발생해야 합니다.");
        } catch (HttpClientErrorException e) {
            checkErrorResponse(e, 404);
        }
    }
}
