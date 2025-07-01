package gift;

import gift.dto.ErrorMessageResponse;
import gift.dto.ProductCreateRequest;
import gift.entity.Product;
import gift.model.CustomPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductControllerTest extends AbstractControllerTest {

    private void checkErrorResponse(HttpClientErrorException e, int expectedStatus) {
        assertThat(e.getStatusCode().value()).isEqualTo(expectedStatus);
        ErrorMessageResponse errorResponse = e.getResponseBodyAs(ErrorMessageResponse.class);
        assertThat(errorResponse).isNotNull();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(expectedStatus, errorResponse.status(), "상태 코드는 " + expectedStatus + "이어야 합니다.");
    }

    @Test
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
        Assertions.assertNotNull(page.contents());
        Assertions.assertNotNull(page.page());
        Assertions.assertNotNull(page.size());
        Assertions.assertNotNull(page.totalElements());
        Assertions.assertNotNull(page.totalPages());
        Assertions.assertEquals(0, (int) page.page(), "기본 페이지는 0이어야 합니다.");
        Assertions.assertEquals(5, (int) page.size(), "기본 페이지 크기는 5이어야 합니다.");

        Assertions.assertTrue(page.contents().size() <= page.size(),
                "페이지의 내용 크기는 페이지 크기보다 작거나 같아야 합니다.");
    }

    @Test
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

    @Test
    public void 제품_생성_시_201_반환() {
        String url = getBaseUrl() + "/api/products";
        ProductCreateRequest request = new ProductCreateRequest("새로운 제품",1000L, "이미지 URL", null);
        ResponseEntity<Product> response = restClient.post()
                .uri(url)
                .body(request)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Product>() {});

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }
    
    @Test
    public void 제품_생성_카카오_md_권한_시_201_반환() {
        String url = getBaseUrl() + "/api/products";
        ProductCreateRequest request = new ProductCreateRequest("MD 권한 카카오 제품", 2000L, "이미지 URL", null);
        ResponseEntity<Product> response = restClient.post()
                .uri(url)
                .header("X-User-Role", "ROLE_MD") // MD 권한으로 요청
                .body(request)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Product>() {});

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void 제품_생성_특정_필드_누락_시_400_반환() {
        String url = getBaseUrl() + "/api/products";
        ProductCreateRequest request = new ProductCreateRequest("새로운 제품", null, "이미지 URL", null); // 가격 필드 누락
        try {
            ResponseEntity<Product> response = restClient.post()
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
    public void 제품_생성_제품이름_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products";
        // 유효하지않은 특수 문자 포함된 15자 이상의 제품 이름
        ProductCreateRequest request = new ProductCreateRequest("<><><><><><><><><><><><><><><><>",
                1000L, "이미지 URL", null); // 유효하지 않은 데이터
        try {
            ResponseEntity<Product> response = restClient.post()
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
    public void 제품_생성_가격_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products";
        ProductCreateRequest request = new ProductCreateRequest("", -1000L, "이미지 URL", null); // 유효하지 않은 데이터
        try {
            ResponseEntity<Product> response = restClient.post()
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
    public void 제품_생성_카카오_유효성_검사_실패_시_400_반환() {
        String url = getBaseUrl() + "/api/products";
        ProductCreateRequest request = new ProductCreateRequest("카카오 제품", 1000L, "이미지 URL", false); // 유효하지 않은 카카오 데이터
        try {
            ResponseEntity<Product> response = restClient.post()
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

    @Test
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