package gift.product;

import gift.AbstractControllerTest;
import gift.entity.Product;
import gift.model.CustomPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductReadTest extends AbstractControllerTest {

    @Test
    @DisplayName("전체 제품 조회 시 200 페이지 반환")
    public void 전체_제품_조회_시_200_페이지_반환() {
        String url = getBaseUrl() + "/api/products";
        ResponseEntity<CustomPage<Product>> response = restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<CustomPage<Product>>() {
                });
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        CustomPage<Product> page = response.getBody();
        assertThat(page).isNotNull();

        Assertions.assertNotNull(page);
        Assertions.assertNotNull(page.page());
        Assertions.assertEquals(0, (int) page.page(), "기본 페이지는 0이어야 합니다.");

        Assertions.assertNotNull(page.size());
        Assertions.assertEquals(5, (int) page.size(), "기본 페이지 크기는 5이어야 합니다.");


        Assertions.assertNotNull(page.contents());
        Assertions.assertTrue(page.contents().size() <= page.size(),
                "페이지의 내용 크기는 페이지 크기보다 작거나 같아야 합니다.");

        Assertions.assertNotNull(page.totalElements());
        Assertions.assertNotNull(page.totalPages());
    }

    @Test
    @DisplayName("전체 제품 조회 음수 페이지 요청 시 400 반환")
    public void 전체_제품_조회_음수_페이지_요청_시_400_반환() {
        String url = getBaseUrl() + "/api/products?page=-1&size=5";
        try {
            ResponseEntity<CustomPage<Product>> response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<CustomPage<Product>>() {});
            Assertions.fail("예외가 발생해야 합니다.");
        } catch (HttpClientErrorException e) {
            checkErrorResponse(e, 400);
        }
    }

    @Test
    @DisplayName("전체 제품 조회 음수 크기 요청 시 400 반환")
    public void 전체_제품_조회_음수_크기_요청_시_400_반환() {
        String url = getBaseUrl() + "/api/products?page=0&size=-1";
        try {
            ResponseEntity<CustomPage<Product>> response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<CustomPage<Product>>() {});
            Assertions.fail("예외가 발생해야 합니다.");
        } catch (HttpClientErrorException e) {
            checkErrorResponse(e, 400);
        }
    }

    @Test
    @DisplayName("특정 제품 조회 시 200 반환")
    public void 특정_제품_조회_시_200_반환() {
        String url = getBaseUrl() + "/api/products/1"; // 존재하는 제품 ID로 변경
        ResponseEntity<Product> response = restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Product>() {});
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("특정 제품 조회 시 404 반환")
    public void 존재하지_않는_제품_조회_시_404_반환() {
        String url = getBaseUrl() + "/api/products/9999"; // 존재하지 않는 제품 ID
        try {
            ResponseEntity<Product> response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Product>() {});
            Assertions.fail("예외가 발생해야 합니다.");
        } catch (HttpClientErrorException e) {
            checkErrorResponse(e, 404);
        }
    }

}

