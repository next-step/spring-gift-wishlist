package gift.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.controller.user.ProductController;
import gift.dto.product.ProductRequest;
import gift.entity.product.Product;
import gift.service.product.ProductService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("GET /api/products - 숨김 제외 리스트 반환")
    void listProducts() throws Exception {
        Product visible = Product.of(1L, "A", 10, "http://example.com/a.png", false);
        Mockito.when(productService.getAllProducts())
                .thenReturn(List.of(visible));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("A")));
    }

    @Test
    @DisplayName("GET /api/products/{id} - ID 조회 성공")
    void getProductById() throws Exception {
        Product p = Product.of(1L, "B", 20, "http://example.com/b.png", false);
        Mockito.when(productService.getProductById(1L))
                .thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("B")));
    }

    @Test
    @DisplayName("GET /api/products/{id} - 숨김 상품은 Not Found")
    void getHiddenProduct() throws Exception {
        Product hidden = Product.of(2L, "C", 30, "http://example.com/c.png", true);
        Mockito.when(productService.getProductById(2L))
                .thenReturn(Optional.of(hidden));

        mockMvc.perform(get("/api/products/{id}", 2L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/products/{id} - 존재하지 않는 ID는 NotFound")
    void getNotFound() throws Exception {
        Mockito.when(productService.getProductById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/products - 생성 성공")
    void createProduct() throws Exception {
        ProductRequest req = new ProductRequest("D", 40, "http://example.com/d.png");
        Product created = Product.of(3L, "D", 40, "http://example.com/d.png", false);
        Mockito.when(productService.createProduct(req.name(), req.price(), req.imageUrl()))
                .thenReturn(created);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("D"));
    }

    @Test
    @DisplayName("PUT /api/products/{id} - 수정 성공")
    void updateProduct() throws Exception {
        ProductRequest req = new ProductRequest("E", 50, "http://example.com/e.png");
        Product updated = Product.of(4L, "E", 50, "http://example.com/e.png", false);

        Mockito.when(productService.getProductById(4L))
                .thenReturn(Optional.of(updated));
        Mockito.when(productService.updateProduct(4L, req.name(), req.price(), req.imageUrl()))
                .thenReturn(updated);

        mockMvc.perform(put("/api/products/{id}", 4L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("E"));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - 삭제 성공")
    void deleteProduct() throws Exception {
        Product existing = Product.of(5L, "D", 50, "http://example.com/d.png", false);
        Mockito.when(productService.getProductById(5L))
                .thenReturn(Optional.of(existing));
        Mockito.doNothing().when(productService).deleteProduct(5L);

        mockMvc.perform(delete("/api/products/{id}", 5L))
                .andExpect(status().isNoContent());
    }
}
