package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 정상적인_상품등록_요청이면_201_응답을_반환한다() throws Exception {
        ProductRequest request = new ProductRequest(
                1L, "허용된이름", 1000, "http://example.com/image.jpg"
        );

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void 상품이름이_15자초과면_400_응답을_반환한다() throws Exception {
        ProductRequest request = new ProductRequest(
                1L, "너무너무너무너무긴상품이름", 1000, "http://example.com/image.jpg"
        );

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("상품 이름은 최대 15자까지 입력 가능합니다."));
    }

    @Test
    void 상품이름에_허용되지않은_특수문자가_있으면_400_응답을_반환한다() throws Exception {
        ProductRequest request = new ProductRequest(
                1L, "이름@에러", 1000, "http://example.com/image.jpg"
        );

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("상품 이름에 허용되지 않는 특수 문자가 포함되어 있습니다."));
    }
}
