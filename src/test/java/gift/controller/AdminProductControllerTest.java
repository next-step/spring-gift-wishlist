package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Product;
import gift.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
public class AdminProductControllerTest {

    @LocalServerPort
    private int port;
    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // 초기화
        List<Product> all = productRepository.findAll();
        all.forEach(p -> productRepository.deleteById(p.id()));

        baseUrl = "http://localhost:" + port + "/admin/products";
    }

    @Test
    void listPage_ShouldReturnProductsInModel() {
        productRepository.save(new Product(null, "A", 10, "uA"));
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("상품 목록");
        assertThat(response.getBody()).contains("A");
    }

    @Test
    void createFormPage_ShouldReturnFormHtml() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/new", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("신규 상품 등록");
        assertThat(response.getBody()).contains("name=\"name\"");
    }

    @Test
    void createProduct_Success_and_MissingField_Fail() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name", "NewProd");
        form.add("price", "100");
        form.add("imageUrl", "http://img");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);
        ResponseEntity<String> resp = restTemplate.postForEntity(baseUrl, entity, String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);

        MultiValueMap<String, String> badForm = new LinkedMultiValueMap<>();
        badForm.add("price", "0");
        badForm.add("imageUrl", "");
        HttpEntity<MultiValueMap<String, String>> badEntity = new HttpEntity<>(badForm, headers);
        ResponseEntity<String> fail = restTemplate.postForEntity(baseUrl, badEntity, String.class);
        assertThat(fail.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        MultiValueMap<String, String> badForm2 = new LinkedMultiValueMap<>();
        badForm2.add("name", "invalid!!");
        badForm2.add("price", "1");
        badForm2.add("imageUrl", "http://img");
        HttpEntity<MultiValueMap<String, String>> badEntity2 = new HttpEntity<>(badForm2, headers);
        ResponseEntity<String> fail2 = restTemplate.postForEntity(baseUrl, badEntity2, String.class);
        assertThat(fail2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        MultiValueMap<String, String> badForm3 = new LinkedMultiValueMap<>();
        badForm3.add("name", "toolongtomakeittoproductname");
        badForm3.add("price", "1");
        badForm3.add("imageUrl", "http://img");
        HttpEntity<MultiValueMap<String, String>> badEntity3 = new HttpEntity<>(badForm3, headers);
        ResponseEntity<String> fail3 = restTemplate.postForEntity(baseUrl, badEntity3, String.class);
        assertThat(fail3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        MultiValueMap<String, String> badForm4 = new LinkedMultiValueMap<>();
        badForm4.add("name", "카카오테크캠퍼스");
        badForm4.add("price", "1");
        badForm4.add("imageUrl", "http://img");
        HttpEntity<MultiValueMap<String, String>> badEntity4 = new HttpEntity<>(badForm4, headers);
        ResponseEntity<String> fail4 = restTemplate.postForEntity(baseUrl, badEntity4, String.class);
        assertThat(fail4.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void editFormPage_and_UpdateProduct_Success_and_NotFound() {
        Product saved = productRepository.save(new Product(null, "EProd", 20, "uE"));
        Long id = saved.id();

        ResponseEntity<String> formResp = restTemplate.getForEntity(baseUrl + "/" + id + "/edit",
                String.class);
        assertThat(formResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(formResp.getBody()).contains("상품 수정");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("_method", "put");
        form.add("name", "EProdX");
        form.add("price", "30");
        form.add("imageUrl", "uX");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

        ResponseEntity<String> updateResp = restTemplate.postForEntity(baseUrl + "/" + id, entity,
                String.class);
        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(productRepository.findById(id).get().name()).isEqualTo("EProdX");

        ResponseEntity<String> notFound = restTemplate.postForEntity(baseUrl + "/999", entity,
                String.class);
        assertThat(notFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteProduct_Success_and_NotFound() {
        Product d = productRepository.save(new Product(null, "DelP", 50, "uD"));
        Long id = d.id();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("_method", "delete");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

        ResponseEntity<String> delResp = restTemplate.postForEntity(baseUrl + "/" + id, entity,
                String.class);
        assertThat(delResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(productRepository.existsById(id)).isFalse();

        ResponseEntity<String> notFound = restTemplate.postForEntity(baseUrl + "/999", entity,
                String.class);
        assertThat(notFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
