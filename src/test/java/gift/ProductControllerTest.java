package gift;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.controller.ProductController;
import gift.dto.request.ProductRequest;
import gift.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("상품명 15자 초과 시 400BAD_REQUEST 에러가 발생한다.")
    @Test
    void 상품명_15자_초과시_400이_반환된다() throws Exception {

        ProductRequest request = new ProductRequest(
                " 0123456789abcdef",
                1000,
                "http://image.jpg"
        );

        assertBadRequest(request, "상품명은 공백 포함 최대 15자까지 입력할 수 있습니다.");
    }

    @DisplayName("허용되지 않은 특수문자 입력 시 400BAD_REQUEST 에러가 발생한다.")
    @Test
    void 허용되지_않은_특수문자_입력시_400이_반환된다() throws Exception{

        ProductRequest request = new ProductRequest(
                " 012345!!!!",
                1000,
                "http://image.jpg"
        );

        assertBadRequest(request,"상품명에는 특수문자 (),[],+,-,&,/,_ 만 포함될 수 있습니다.");
    }

    @DisplayName("상품명에 '카카오' 포함 시 400BAD_REQUEST 에러가 발생한다.")
    @Test
    void 상품명에_카카오_포함시_400이_반환된다() throws Exception{

        ProductRequest request = new ProductRequest(
                "카카오97 초콜릿",
                1000,
                "http://image.jpg"
        );

        assertBadRequest(request,"상품명에 '카카오'를 포함할 수 없습니다. 담당자에게 문의하세요.");
    }

    private void assertBadRequest(ProductRequest request, String expectedMessage) throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name[0]").value(expectedMessage));
    }
}