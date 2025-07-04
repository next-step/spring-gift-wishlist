package gift.domain.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.application.product.ProductApplicationService;
import gift.domain.product.dto.ProductRequest;
import gift.domain.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Mock
    private ProductApplicationService productApplicationService;

    @InjectMocks
    private ProductController productController;

    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        
        productRequest = ProductRequest.of("감자", 1000, "https://test.com/img.jpg");
    }

    @Test
    @DisplayName("상품 추가 - 성공")
    void addProduct() throws Exception {
        String content = objectMapper.writeValueAsString(productRequest);
        ProductResponse responseDto = ProductResponse.of(1L, "감자", 1000, "https://test.com/img.jpg");
        given(productApplicationService.addProduct(any(ProductRequest.class))).willReturn(responseDto);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("상품 조회 - 성공")
    void getProduct() throws Exception {
        Long productId = 1L;
        ProductResponse responseDto = ProductResponse.of(productId, "감자", 1000, "https://test.com/img.jpg");
        given(productApplicationService.getProduct(eq(productId))).willReturn(responseDto);

        mockMvc.perform(get("/api/products/{id}", productId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("감자"));
    }

    @Test
    @DisplayName("상품 수정 - 성공")
    void updateProduct() throws Exception {
        Long productId = 1L;
        ProductRequest updateRequest = ProductRequest.of("고구마", 2000, "https://test.com/img2.jpg");
        String content = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("상품 삭제 - 성공")
    void deleteProduct() throws Exception {
        Long productId = 1L;

        mockMvc.perform(delete("/api/products/{id}", productId))
            .andExpect(status().isNoContent());
    }
}