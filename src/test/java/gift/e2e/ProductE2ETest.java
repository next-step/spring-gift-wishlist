package gift.e2e;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.product.ProductRequest;
import gift.e2e.fixture.ProductFixture;
import gift.entity.product.Product;
import gift.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class ProductE2ETest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        productRepository.findAll()
                .forEach(p -> productRepository.deleteById(p.id().id()));
    }

    @Nested
    @DisplayName("GET /api/products")
    class ListTests {

        @Test
        void listExcludesHidden() throws Exception {
            ProductFixture.save(productRepository, "V1", 10, "http://example.com/v1.png");
            Product hidden = ProductFixture.save(productRepository, "V2", 20,
                            "http://example.com/v2.png")
                    .withHidden(true);
            productRepository.save(hidden);

            mockMvc.perform(get("/api/products"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("V1"))
                    .andExpect(jsonPath("$[1]").doesNotExist());
        }
    }

    @Nested
    @DisplayName("GET /api/products/{id}")
    class GetByIdTests {

        @Test
        void getVisible() throws Exception {
            Product p = ProductFixture.save(productRepository, "P1", 5,
                    "http://example.com/p1.png");
            mockMvc.perform(get("/api/products/{id}", p.id().id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("P1"));
        }

        @Test
        void getHidden() throws Exception {
            Product hidden = ProductFixture.save(productRepository, "P2", 5,
                            "http://example.com/p2.png")
                    .withHidden(true);
            productRepository.save(hidden);
            mockMvc.perform(get("/api/products/{id}", hidden.id().id()))
                    .andExpect(status().isNotFound());
        }

        @Test
        void getNotFound() throws Exception {
            mockMvc.perform(get("/api/products/999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /api/products")
    class CreateTests {

        @Test
        void createValid() throws Exception {
            ProductRequest req = new ProductRequest("N1", 10, "http://example.com/n1.png");
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("N1"));
        }

        @Test
        void createHidden() throws Exception {
            ProductRequest req = new ProductRequest("카카오상품", 10, "http://example.com/kakao.png");
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isCreated());
            mockMvc.perform(get("/api/products"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        void createInvalid() throws Exception {
            ProductRequest req = new ProductRequest("", -1, "");
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT /api/products/{id}")
    class UpdateTests {

        @Test
        void updateValid() throws Exception {
            Product p = ProductFixture.save(productRepository, "U1", 15,
                    "http://example.com/u1.png");
            ProductRequest req = new ProductRequest("UX", 20, "http://example.com/ux.png");
            mockMvc.perform(put("/api/products/{id}", p.id().id())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("UX"));
        }

        @Test
        void updateHidden() throws Exception {
            Product hidden = ProductFixture.save(productRepository, "H1", 15,
                            "http://example.com/h1.png")
                    .withHidden(true);
            productRepository.save(hidden);
            ProductRequest req = new ProductRequest("HX", 20, "http://example.com/hx.png");
            mockMvc.perform(put("/api/products/{id}", hidden.id().id())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void updateNotFound() throws Exception {
            ProductRequest req = new ProductRequest("X", 1, "http://example.com/x.png");
            mockMvc.perform(put("/api/products/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void updateInvalid() throws Exception {
            Product p = ProductFixture.save(productRepository, "U2", 15,
                    "http://example.com/u2.png");
            ProductRequest req = new ProductRequest("", -1, "");
            mockMvc.perform(put("/api/products/{id}", p.id().id())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE /api/products/{id}")
    class DeleteTests {

        @Test
        void deleteValid() throws Exception {
            Product p = ProductFixture.save(productRepository, "D1", 5,
                    "http://example.com/d1.png");
            mockMvc.perform(delete("/api/products/{id}", p.id().id()))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deleteHidden() throws Exception {
            Product hidden = ProductFixture.save(productRepository, "HD1", 5,
                            "http://example.com/hd1.png")
                    .withHidden(true);
            productRepository.save(hidden);
            mockMvc.perform(delete("/api/products/{id}", hidden.id().id()))
                    .andExpect(status().isNotFound());
        }

        @Test
        void deleteNotFound() throws Exception {
            mockMvc.perform(delete("/api/products/999"))
                    .andExpect(status().isNotFound());
        }
    }
}
