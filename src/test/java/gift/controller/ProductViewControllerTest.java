package gift.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import gift.dto.request.ProductCreateRequestDto;
import gift.dto.response.ProductGetResponseDto;
import gift.service.ProductService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProductViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductGetResponseDto sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new ProductGetResponseDto(
            1L,
            "테스트 상품",
            10000.0,
            "https://test.jpg",
            true
        );
    }

    @Test
    void 단건상품등록_페이지접속() throws Exception {
        mockMvc.perform(get("/admin/products/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("create-product"));
    }

    @Test
    void 단건상품등록_성공() throws Exception {
        mockMvc.perform(post("/admin/products/create")
                .param("name", "상품A")
                .param("price", "10000")
                .param("imageUrl", "https://1.img")
                .param("mdConfirmed", "false"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/products"));

        verify(productService, times(1)).saveProduct(any(ProductCreateRequestDto.class));
    }

    @Test
    void 단건상품등록_유효성검사_실패() throws Exception {
        mockMvc.perform(post("/admin/products/create")
                .param("name", "12345 12345 12345") // 15자 초과 유효성 실패 유도
                .param("price", "10000")
                .param("imageUrl", "https://1.img")
                .param("mdConfirmed", "false"))
            .andExpect(status().isOk())
            .andExpect(view().name("create-product"))
            .andExpect(model().attributeExists("errors", "productCreateRequestDto"));
    }

    @Test
    void 단건상품등록_실패() throws Exception {
        doThrow(new RuntimeException("DB 오류")).when(productService).saveProduct(any());

        mockMvc.perform(post("/admin/products/create")
                .param("name", "상품A")
                .param("price", "10000")
                .param("imageUrl", "url.jpg")
                .param("mdConfirmed", "true"))
            .andExpect(status().isOk())
            .andExpect(view().name("create-product"))
            .andExpect(model().attributeExists("errorMessage"));

    }

    @Test
    void 상품조회_페이지접속() throws Exception {
        when(productService.findAllProducts()).thenReturn(List.of(sampleProduct));

        mockMvc.perform(get("/admin/products"))
            .andExpect(status().isOk())
            .andExpect(view().name("products"))
            .andExpect(model().attributeExists("products"));
    }

    @Test
    void 단건상품수정_성공() throws Exception {
        when(productService.findProductById(1L)).thenReturn(sampleProduct);

        mockMvc.perform(get("/admin/products/update/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("update-product"))
            .andExpect(model().attributeExists("product"));
    }

    @Test
    void 단건상품수정_실패() throws Exception {
        when(productService.findProductById(1L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/admin/products/update/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/products"));
    }

    @Test
    void 단건상품삭제_성공() throws Exception {
        mockMvc.perform(post("/admin/products/delete/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/products"));

        verify(productService, times(1)).deleteProductById(1L);
    }

    @Test
    void 단건상품삭제_실패() throws Exception {
        doThrow(new RuntimeException()).when(productService).deleteProductById(1L);

        mockMvc.perform(post("/admin/products/delete/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/products"));
    }


}