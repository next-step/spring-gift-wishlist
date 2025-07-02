package gift.product;

import gift.AbstractControllerTest;
import gift.dto.ProductCreateRequest;
import gift.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductUpdateTest extends AbstractControllerTest {

    @Test
    public void 제품_수정_시_200_반환() {
        String url = getBaseUrl() + "/api/products/1"; // 존재하는 제품 ID로 변경
        ProductCreateRequest request = new ProductCreateRequest("수정된 제품", 1500L, "수정된 이미지 URL", null);
        ResponseEntity<Product> response = restClient.put()
                .uri(url)
                .body(request)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Product>() {});

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void 제품_수정_특정_필드_누락_시_200_반환() {
        String url = getBaseUrl() + "/api/products/1"; // 존재하는 제품 ID로 변경
        ProductCreateRequest request = new ProductCreateRequest("수정된 제품", null, "수정된 이미지 URL", null); // 가격 필드 누락
        ResponseEntity<Product> response = restClient.put()
                .uri(url)
                .body(request)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Product>() {});
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void 제품_수정_카카오_md_권한_시_200_반환() {
        String url = getBaseUrl() + "/api/products/1"; // 존재하는 제품 ID로 변경
        ProductCreateRequest request = new ProductCreateRequest("MD 권한 카카오 수정 제품", 2000L, "수정된 이미지 URL", null);
        ResponseEntity<Product> response = restClient.put()
                .uri(url)
                .header("X-User-Role", "ROLE_MD") // MD 권한으로 요청
                .body(request)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Product>() {});

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void 제품_수정_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products/1"; // 존재하는 제품 ID로 변경
        ProductCreateRequest request = new ProductCreateRequest("<><><><><><><><><><><><><>",
                1000L, "이미지 URL", null); // 유효하지 않은 데이터
        try {
            ResponseEntity<Product> response = restClient.put()
                    .uri(url)
                    .body(request)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Product>() {
                    });
            Assertions.fail("예외가 발생해야 합니다.");
        } catch (HttpClientErrorException e) {
            checkErrorResponse(e, 400);
        }
    }

    @Test
    public void 제품_수정_카카오_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products/1"; // 존재하는 제품 ID로 변경
        ProductCreateRequest request = new ProductCreateRequest("카카오 수정 제품", 1000L, "이미지 URL", false); // 유효하지 않은 카카오 데이터
        try {
            ResponseEntity<Product> response = restClient.put()
                    .uri(url)
                    .body(request)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Product>() {});
            Assertions.fail("예외가 발생해야 합니다.");
        } catch (HttpClientErrorException e) {
            checkErrorResponse(e, 400);
        }
    }

    @Test
    public void 제품_패치_특정_필드_누락_시_200_반환() {
        String url = getBaseUrl() + "/api/products/1"; // 존재하는 제품 ID로 변경
        ProductCreateRequest request = new ProductCreateRequest("수정된 제품", null, "www.modified.com", null); // 가격 필드 누락
        ResponseEntity<Product> response = restClient.patch()
                .uri(url)
                .body(request)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Product>() {});
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void 제품_패치_카카오_md_권한_시_200_반환() {
        String url = getBaseUrl() + "/api/products/1"; // 존재하는 제품 ID로 변경
        ProductCreateRequest request = new ProductCreateRequest("MD 권한 카카오 수정 제품", 2000L, "www.modified.com", true);
        ResponseEntity<Product> response = restClient.patch()
                .uri(url)
                .header("X-User-Role", "ROLE_MD") // MD 권한으로 요청
                .body(request)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Product>() {});

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void 제품_패치_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products/1"; // 존재하는 제품 ID로 변경
        ProductCreateRequest request = new ProductCreateRequest("<><><><><><><><><><><><><>",
                1000L, "이미지 URL", null); // 유효하지 않은 데이터
        try {
            ResponseEntity<Product> response = restClient.patch()
                    .uri(url)
                    .body(request)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Product>() {
                    });
            Assertions.fail("예외가 발생해야 합니다.");
        } catch (HttpClientErrorException e) {
            checkErrorResponse(e, 400);
        }
    }

    @Test
    public void 제품_패치_카카오_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products/1"; // 존재하는 제품 ID로 변경
        ProductCreateRequest request = new ProductCreateRequest("카카오 수정 제품", 1000L, "이미지 URL", false); // 유효하지 않은 카카오 데이터
        try {
            ResponseEntity<Product> response = restClient.patch()
                    .uri(url)
                    .body(request)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Product>() {});
            Assertions.fail("예외가 발생해야 합니다.");
        } catch (HttpClientErrorException e) {
            checkErrorResponse(e, 400);
        }
    }
}
