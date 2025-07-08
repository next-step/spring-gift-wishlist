package gift.e2e;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class AdminProductE2ETest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void clean() {
        productRepository.findAll()
                .forEach(p -> productRepository.deleteById(p.id().id()));
    }

    @Nested
    @DisplayName("View Pages")
    class ViewTests {

        @Test
        @DisplayName("상품 목록")
        void listPage() throws Exception {
            ProductFixture.save(productRepository, "A", 10, "http://example.com/a.png");

            mockMvc.perform(get("/admin/products"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("상품 목록")))
                    .andExpect(content().string(containsString("A")));
        }

        @Test
        @DisplayName("신규 등록 폼")
        void createFormPage() throws Exception {
            mockMvc.perform(get("/admin/products/new"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("신규 상품 등록")))
                    .andExpect(content().string(containsString("name=\"name\"")));
        }

        @Test
        @DisplayName("수정 폼")
        void editFormPage() throws Exception {
            Product p = ProductFixture.save(productRepository, "EProd", 20,
                    "http://example.com/e.png");
            mockMvc.perform(get("/admin/products/{id}/edit", p.id().id()))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("상품 수정")))
                    .andExpect(content().string(containsString("value=\"EProd\"")));
        }
    }

    @Nested
    @DisplayName("Create Product")
    class CreateTests {

        @Test
        @DisplayName("유효 입력 성공")
        void createValid() throws Exception {
            mockMvc.perform(post("/admin/products/new")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("name", "NewProd")
                            .param("price", "100")
                            .param("imageUrl", "http://example.com/new.png"))
                    .andExpect(status().is3xxRedirection());
        }

        @Test
        @DisplayName("필드 누락 BAD_REQUEST")
        void createMissingField() throws Exception {
            mockMvc.perform(post("/admin/products/new")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("price", "0")
                            .param("imageUrl", ""))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("name 필드 오류 검출")
        void createInvalidName() throws Exception {
            mockMvc.perform(post("/admin/products/new")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("name", "invalid!!")
                            .param("price", "12")
                            .param("imageUrl", "http://example.com/invalid.png"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("가격 필드 오류 검출")
        void createInvalidPrice() throws Exception {
            mockMvc.perform(post("/admin/products/new")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("name", "valid")
                            .param("price", "0")
                            .param("imageUrl", "http://example.com/new.png"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("url 필드 오류 검출")
        void createInvalidUrl() throws Exception {
            mockMvc.perform(post("/admin/products/new")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("name", "valid")
                            .param("price", "12")
                            .param("imageUrl", "http://example"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("관리자 허용 패턴 이름 성공")
        void createAdminPattern() throws Exception {
            mockMvc.perform(post("/admin/products/new")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("name", "카카오")
                            .param("price", "50")
                            .param("imageUrl", "http://example.com/pattern.png"))
                    .andExpect(status().is3xxRedirection());
        }
    }

    @Nested
    @DisplayName("Update Product")
    class UpdateTests {

        @Test
        @DisplayName("정상 수정")
        void updateValid() throws Exception {
            Product p = ProductFixture.save(productRepository, "EProd", 20,
                    "http://example.com/e.png");
            mockMvc.perform(post("/admin/products/{id}", p.id().id())
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("_method", "put")
                            .param("name", "EProdX")
                            .param("price", "30")
                            .param("imageUrl", "http://example.com/updated.png"))
                    .andExpect(status().is3xxRedirection());
        }

        @Test
        @DisplayName("존재하지 않는 ID 수정 시 NOT_FOUND")
        void updateNotFound() throws Exception {
            mockMvc.perform(post("/admin/products/999")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("_method", "put")
                            .param("name", "X")
                            .param("price", "1")
                            .param("imageUrl", "http://example.com/x.png"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("name 필드 오류 검출")
        void updateInvalidName() throws Exception {
            Product p = ProductFixture.save(productRepository, "EProd", 20,
                    "http://example.com/e.png");
            mockMvc.perform(post("/admin/products/{id}", p.id().id())
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("_method", "put")
                            .param("name", "invalid!!")
                            .param("price", "12")
                            .param("imageUrl", "http://example.com/invalid.png"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("가격 필드 오류 검출")
        void updateInvalidPrice() throws Exception {
            Product p = ProductFixture.save(productRepository, "EProd", 20,
                    "http://example.com/e.png");
            mockMvc.perform(post("/admin/products/{id}", p.id().id())
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("_method", "put")
                            .param("name", "valid")
                            .param("price", "0")
                            .param("imageUrl", "http://example.com/new.png"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("url 필드 오류 검출")
        void updateInvalidUrl() throws Exception {
            Product p = ProductFixture.save(productRepository, "EProd", 20,
                    "http://example.com/e.png");
            mockMvc.perform(post("/admin/products/{id}", p.id().id())
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("_method", "put")
                            .param("name", "valid")
                            .param("price", "12")
                            .param("imageUrl", "http://example"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("관리자 허용 패턴 이름 성공")
        void updateAdminPattern() throws Exception {
            Product p = ProductFixture.save(productRepository, "EProd", 20,
                    "http://example.com/e.png");
            mockMvc.perform(post("/admin/products/{id}", p.id().id())
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("_method", "put")
                            .param("name", "카카오")
                            .param("price", "50")
                            .param("imageUrl", "http://example.com/pattern.png"))
                    .andExpect(status().is3xxRedirection());
        }
    }

    @Nested
    @DisplayName("Delete Product")
    class DeleteTests {

        @Test
        @DisplayName("정상 삭제")
        void deleteValid() throws Exception {
            Product p = ProductFixture.save(productRepository, "DelP", 50,
                    "http://example.com/del.png");
            mockMvc.perform(post("/admin/products/{id}", p.id().id())
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("_method", "delete"))
                    .andExpect(status().is3xxRedirection());
        }

        @Test
        @DisplayName("존재하지 않는 ID 삭제 시 NOT_FOUND")
        void deleteNotFound() throws Exception {
            mockMvc.perform(post("/admin/products/999")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("_method", "delete"))
                    .andExpect(status().isNotFound());
        }
    }
}
