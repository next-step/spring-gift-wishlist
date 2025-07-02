package gift;

import gift.controller.AdminPageController;
import gift.entity.Product;
import gift.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminPageController.class)
class AdminPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void 상품목록_조회_시_상품목록페이지() throws Exception {
        mockMvc.perform(get("/admin/products"))
            .andExpect(view().name("admin/product-list"))
            .andExpect(model().attributeExists("products"));
    }

    @Test
    void 상품등록_폼_요청_시_상품상세페이지() throws Exception {
        mockMvc.perform(get("/admin/products/new"))
            .andExpect(view().name("admin/product-form"))
            .andExpect(model().attributeExists("product"));
        // productId도 null로써 값 자체는 제공되나, attributeExists 내부에서 nullable 검사 수행하므로 배제
    }

    @Test
    void 유효한_상품등록_폼_제출_시_리다이렉션() throws Exception {
        Product mockProduct = new Product(
            1L, "아이스 카페 아메리카노 T", 4700,
            "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
            false
        );
        when(productService.createProduct(any(), any(), any()))
            .thenReturn(mockProduct);

        mockMvc.perform(
            post("/admin/products")
                .param("name", mockProduct.getName())
                .param("price", String.valueOf(mockProduct.getPrice()))
                .param("imageUrl", mockProduct.getImageUrl())
            )
            .andExpect(redirectedUrl("/admin/products/" + mockProduct.getId()));
    }

    @Test
    void 유효하지_않은_상품등록_폼_제출_시_상품상세페이지() throws Exception {
        Product mockProduct = new Product(
            1L, "&%&각하오커피&%&", -5000,
            "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
            false // validated
        );
        when(productService.createProduct(any(), any(), any()))
            .thenReturn(mockProduct);

        mockMvc.perform(
            post("/admin/products")
                .param("name", mockProduct.getName())
                .param("price", String.valueOf(mockProduct.getPrice()))
                .param("imageUrl", mockProduct.getImageUrl())
            )
            .andExpect(view().name("admin/product-form"))
            .andExpect(model().attributeHasErrors("productRequestDto"));
    }

    @Test
    void 상품_벨리데이션_수정_시_리다이렉션() throws Exception {
        mockMvc.perform(
            patch("/admin/products/1")
                .queryParam("validated", "true")
            )
            .andExpect(redirectedUrl("/admin/products/1"));
    }

    @Test
    void 유효한_상품_조회_시_상품상세페이지() throws Exception {
        Product mockProduct = new Product(1L, "각하오커피", 7800,
            "https://...", false);
        when(productService.getProductById(1L)).thenReturn(mockProduct);

        mockMvc.perform(get("/admin/products/1"))
            .andExpect(view().name("admin/product-form"));

    }

    @Test
    void 유효한_상품_수정_시_리다이렉션() throws Exception {
        Product updated = new Product(1L, "각하오 커피", 7800, "https://...", false);
        when(productService.updateProductById(eq(1L), any(), any(), any()))
                .thenReturn(updated);

        mockMvc.perform(
            put("/admin/products/1")
                .param("name", "각하오 커피")
                .param("price", "7800")
                .param("imageUrl", "https://...")
            )
            .andExpect(redirectedUrl("/admin/products/1"));

    }

    @Test
    void 유효하지_않은_상품_수정_시_상품상세페이지() throws Exception {
        Product existing = new Product(1L, "각하오 커피", 7800, "https://...", false);
        when(productService.getProductById(1L)).thenReturn(existing);

        mockMvc.perform(
            put("/admin/products/1")
                .param("name", "%&각하오커피&%")
                .param("price", "-5800")
                .param("imageUrl", "")
            )
            .andExpect(view().name("admin/product-form"))
            .andExpect(model().attributeHasErrors("productRequestDto"));
    }

    @Test
    void 유효한_상품_삭제_시_리다이렉션() throws Exception {
        mockMvc.perform(delete("/admin/products/1"))
            .andExpect(redirectedUrl("/admin/products"));
    }
}