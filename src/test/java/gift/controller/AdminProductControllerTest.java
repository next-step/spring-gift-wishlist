package gift.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.controller.product.AdminProductController;
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

@WebMvcTest(AdminProductController.class)
class AdminProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("상품 목록 뷰 반환")
    void listPage() throws Exception {
        Product p = Product.of(1L, "A", 10, "http://example.com/a.png", false);
        Mockito.when(productService.getAllProducts()).thenReturn(List.of(p));

        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("상품 목록")))
                .andExpect(content().string(containsString("A")));
    }

    @Test
    @DisplayName("신규 등록 폼 뷰 반환")
    void createFormPage() throws Exception {
        mockMvc.perform(get("/admin/products/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("신규 상품 등록")))
                .andExpect(content().string(containsString("name=\"name\"")));
    }

    @Test
    @DisplayName("상품 등록 처리 - 성공")
    void createValid() throws Exception {
        Mockito.when(productService.createProduct(eq("NewProd"), eq(100),
                        eq("http://example.com/new.png")))
                .thenReturn(Product.of(1L, "NewProd", 100, "http://example.com/new.png", false));

        mockMvc.perform(post("/admin/products/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "NewProd")
                        .param("price", "100")
                        .param("imageUrl", "http://example.com/new.png"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("상품 수정 폼 뷰 반환")
    void editFormPage() throws Exception {
        Product p = Product.of(2L, "EProd", 20, "http://example.com/e.png", false);
        Mockito.when(productService.getProductById(2L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/admin/products/{id}/edit", 2L))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("상품 수정")))
                .andExpect(content().string(containsString("value=\"EProd\"")));
    }

    @Test
    @DisplayName("상품 수정 처리 - REDIRECT")
    void updateNotFound() throws Exception {
        Mockito.when(productService.getProductById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/admin/products/{id}", 999)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("_method", "put")
                        .param("name", "X")
                        .param("price", "1")
                        .param("imageUrl", "http://example.com/x.png"))
                .andExpect(status().is3xxRedirection());
    }
}
