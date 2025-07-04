package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Product;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class AdminProductControllerTest {

    @LocalServerPort
    private int port;
    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.findAll()
                .forEach(p -> productRepository.deleteById(p.id()));
        baseUrl = "http://localhost:" + port + "/admin/products";
    }

    @Nested
    @DisplayName("View Pages")
    class ViewTests {

        @Test
        @DisplayName("GET list page - 상품 목록 조회")
        void listPage_ShouldReturnProductsInModel() {
            productRepository.save(new Product(null, "A", 10, "uA"));

            ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("상품 목록");
            assertThat(response.getBody()).contains("A");
        }

        @Test
        @DisplayName("GET create form page - 신규 등록 폼 조회")
        void createFormPage_ShouldReturnFormHtml() {
            ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/new",
                    String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("신규 상품 등록");
            assertThat(response.getBody()).contains("name=\"name\"");
        }

        @Test
        @DisplayName("GET edit form page - 수정 폼 조회")
        void editFormPage_ShouldReturnEditForm() {
            Product saved = productRepository.save(new Product(null, "EProd", 20, "uE"));
            Long id = saved.id();

            ResponseEntity<String> formResp = restTemplate.getForEntity(
                    baseUrl + "/" + id + "/edit", String.class);

            assertThat(formResp.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(formResp.getBody()).contains("상품 수정");
        }
    }

    @Nested
    @DisplayName("Create Product")
    class CreateTests {

        private HttpHeaders headers;
        private MultiValueMap<String, String> form;

        @BeforeEach
        void init() {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            form = new LinkedMultiValueMap<>();
        }

        @Test
        @DisplayName("POST create - 유효한 입력 성공")
        void createProduct_ValidInput_ReturnsOk() {
            form.add("name", "NewProd");
            form.add("price", "100");
            form.add("imageUrl", "http://img");

            ResponseEntity<String> resp = restTemplate.postForEntity(baseUrl,
                    new HttpEntity<>(form, headers), String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        @DisplayName("POST create - 관리자 허용 패턴 이름 성공")
        void createProduct_AdminPatternName_ReturnsOk() {
            form.add("name", "()[]_유+효-문&자");
            form.add("price", "50");
            form.add("imageUrl", "http://img");

            ResponseEntity<String> resp = restTemplate.postForEntity(baseUrl,
                    new HttpEntity<>(form, headers), String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        @DisplayName("POST create - 필드 누락 시 BAD_REQUEST")
        void createProduct_MissingField_ReturnsBadRequest() {
            form.add("price", "0");
            form.add("imageUrl", "");

            ResponseEntity<String> resp = restTemplate.postForEntity(baseUrl,
                    new HttpEntity<>(form, headers), String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("POST create - 패턴 불일치 name 시 BAD_REQUEST")
        void createProduct_InvalidPattern_ReturnsBadRequest() {
            form.add("name", "invalid!!");
            form.add("price", "1");
            form.add("imageUrl", "http://img");

            ResponseEntity<String> resp = restTemplate.postForEntity(baseUrl,
                    new HttpEntity<>(form, headers), String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("POST create - 이름 길이 초과 시 BAD_REQUEST")
        void createProduct_NameTooLong_ReturnsBadRequest() {
            form.add("name", "toolongtomakeittoproductname");
            form.add("price", "1");
            form.add("imageUrl", "http://img");

            ResponseEntity<String> resp = restTemplate.postForEntity(baseUrl,
                    new HttpEntity<>(form, headers), String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("POST create - 관리문구 포함 name 시 OK")
        void createProduct_ManagementName_ReturnsBadRequest() {
            form.add("name", "카카오테크캠퍼스");
            form.add("price", "1");
            form.add("imageUrl", "http://img");

            ResponseEntity<String> resp = restTemplate.postForEntity(baseUrl,
                    new HttpEntity<>(form, headers), String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    @Nested
    @DisplayName("Update Product")
    class UpdateTests {

        private HttpHeaders headers;
        private MultiValueMap<String, String> form;
        private Long existingId;

        @BeforeEach
        void init() {
            Product saved = productRepository.save(new Product(null, "EProd", 20, "uE"));
            existingId = saved.id();

            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            form = new LinkedMultiValueMap<>();
            form.add("_method", "put");
            form.add("name", "EProdX");
            form.add("price", "30");
            form.add("imageUrl", "uX");
        }

        @Test
        @DisplayName("POST update - 유효한 관리자 패턴 이름 수정 성공")
        void updateProduct_AdminPatternName_ReturnsOk() {
            form.set("name", "()[]_유+효-문&자");

            ResponseEntity<String> resp = restTemplate.postForEntity(
                    baseUrl + "/" + existingId, new HttpEntity<>(form, headers), String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(productRepository.findById(existingId).get().name()).isEqualTo(
                    "()[]_유+효-문&자");
        }

        @Test
        @DisplayName("POST update - 정상 수정 성공")
        void updateProduct_ValidInput_ReturnsOk() {
            ResponseEntity<String> resp = restTemplate.postForEntity(
                    baseUrl + "/" + existingId, new HttpEntity<>(form, headers), String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(productRepository.findById(existingId).get().name()).isEqualTo("EProdX");
        }

        @Test
        @DisplayName("POST update - 존재하지 않는 ID 시 NOT_FOUND")
        void updateProduct_NotFound_ReturnsNotFound() {
            ResponseEntity<String> resp = restTemplate.postForEntity(
                    baseUrl + "/999", new HttpEntity<>(form, headers), String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("POST update - 패턴 불일치 name 시 BAD_REQUEST")
        void updateProduct_InvalidPattern_ReturnsBadRequest() {
            form.set("name", "invalid!!");

            ResponseEntity<String> resp = restTemplate.postForEntity(
                    baseUrl + "/" + existingId,
                    new HttpEntity<>(form, headers),
                    String.class
            );

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("POST update - 이름 길이 초과 시 BAD_REQUEST")
        void updateProduct_NameTooLong_ReturnsBadRequest() {
            form.set("name", "toolongtomakeittoproductname");

            ResponseEntity<String> resp = restTemplate.postForEntity(
                    baseUrl + "/" + existingId,
                    new HttpEntity<>(form, headers),
                    String.class
            );

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("POST update - 관리문구 포함 name 시 OK")
        void updateProduct_ManagementName_ReturnsOk() {
            form.set("name", "카카오테크캠퍼스");

            ResponseEntity<String> resp = restTemplate.postForEntity(
                    baseUrl + "/" + existingId,
                    new HttpEntity<>(form, headers),
                    String.class
            );

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        }


    }

    @Nested
    @DisplayName("Delete Product")
    class DeleteTests {

        private HttpHeaders headers;
        private MultiValueMap<String, String> form;
        private Long existingId;

        @BeforeEach
        void init() {
            Product saved = productRepository.save(new Product(null, "DelP", 50, "uD"));
            existingId = saved.id();

            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            form = new LinkedMultiValueMap<>();
            form.add("_method", "delete");
        }

        @Test
        @DisplayName("POST delete - 정상 삭제 성공")
        void deleteProduct_ExistingId_ReturnsOk() {
            ResponseEntity<String> resp = restTemplate.postForEntity(
                    baseUrl + "/" + existingId, new HttpEntity<>(form, headers), String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(productRepository.existsById(existingId)).isFalse();
        }

        @Test
        @DisplayName("POST delete - 존재하지 않는 ID 삭제 시 NOT_FOUND")
        void deleteProduct_NotFound_ReturnsNotFound() {
            ResponseEntity<String> resp = restTemplate.postForEntity(
                    baseUrl + "/999", new HttpEntity<>(form, headers), String.class);

            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
