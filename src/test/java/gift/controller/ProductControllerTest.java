package gift.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductService productService;

    @Test
    @DisplayName("정상 생성")
    void createProduct_success() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("초코케이크", 10000,
                "http://img.com/image.jpg");

        Mockito.when(productService.save(Mockito.any()))
                .thenReturn(new ProductResponseDto(1L, "초코케이크", 10000, "http://img.com/image.jpg"));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("상품명 15자 초과")
    void createProduct_nameTooLong() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("상품명 15자 초과상품명 15자 초과", 10000,
                "http://img.com/image.jpg");

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("상품명 최대 15자 입력 가능"));
    }

    @Test
    @DisplayName("특수 문자 포함")
    void createProduct_invalidCharacters() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("@", 10000,
                "http://img.com/image.jpg");

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("허용되지 않은 특수문자가 포함"));
    }

    @Test
    @DisplayName("카카오 포함")
    void createProduct_nameContainsKakao() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("카카오", 10000,
                "http://img.com/image.jpg");

        Mockito.when(productService.save(Mockito.any()))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST,
                        "\"카카오\"가 포함된 상품명 사용 불가"
                ));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("\"카카오\"가 포함된 상품명 사용 불가"));
    }
}
