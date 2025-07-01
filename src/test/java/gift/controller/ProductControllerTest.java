package gift.controller;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.model.Product;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll(); // 테스트마다 DB 초기화
    }

    @Test
    void 상품_등록_성공() throws Exception {
        // given
        Product product = new Product();
        product.setName("아이스 아메리카노");
        product.setPrice(2500);
        product.setImageUrl("americano.jpg");

        // when & then
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))).andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.name").value("아이스 아메리카노"))
            .andExpect(jsonPath("$.price").value(2500))
            .andExpect(jsonPath("$.imageUrl").value("americano.jpg"));
    }

    @Test
    void 전체_상품_조회() throws Exception {
        // given
        productRepository.save(new Product(null, "아이스 아메리카노", 2500, "americano.jpg"));
        productRepository.save(new Product(null, "아이스티", 3000, "iceTea.jpg"));

        // when & then
        mockMvc.perform(get("/api/products")).andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(2)))
            .andExpect(jsonPath("$[0].name", not(emptyString())));
    }

    @Test
    void 특정_상품_조회() throws Exception {
        // given
        Product saved = productRepository.save(
            new Product(null, "아이스 아메리카노", 2500, "americano.jpg"));

        // when & then
        mockMvc.perform(get("/api/products/" + saved.getId())).andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("아이스 아메리카노"));
    }

    @Test
    void 상품_수정() throws Exception {
        Product saved = productRepository.save(
            new Product(null, "아이스 아메리카노", 2500, "americano.jpg"));

        Product updated = new Product();
        updated.setName("핫 아메리카노");
        updated.setPrice(2000);
        updated.setImageUrl("americano.jpg");

        mockMvc.perform(
                put("/api/products/" + saved.getId()).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isNoContent());
    }

    @Test
    void 상품_삭제() throws Exception {
        Product saved = productRepository.save(
            new Product(null, "아이스 아메리카노", 2500, "americano.jpg"));

        mockMvc.perform(delete("/api/products/" + saved.getId())).andExpect(status().isNoContent());
    }
}
