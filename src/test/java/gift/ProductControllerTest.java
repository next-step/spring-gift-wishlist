package gift;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.api.ProductCreateRequestDto;
import gift.entity.ApprovedProduct;
import gift.repository.ApprovedProductRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ApprovedProductRepository approvedProductRepository;

    @Test
    @DisplayName("[API] 상품 등록 성공 - '카카오' 포함된 승인된 상품명")
    void createProduct_success_withApprovedName() throws Exception {

        // '카카오' 포함된 승인된 상품명 추가
        approvedProductRepository.save(new ApprovedProduct("카카오 프렌즈 볼펜"));

        var dto = new ProductCreateRequestDto("카카오 프렌즈 볼펜", 15000, "https://image.com/item.jpg");

        // when & then
        mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("[API] 상품 등록 실패 - '카카오' 포함 & 승인되지 않은 상품명")
    void createProduct_fail_unapprovedKakaoName() throws Exception {

        var dto = new ProductCreateRequestDto("카카오 지갑", 15000, "https://image.com/item.jpg");

        // when & then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("'카카오'가 포함된 상품은 MD 승인이 필요합니다."));
    }

    @Test
    @DisplayName("[API] 상품 등록 실패 - 상품명 없음")
    void createProduct_fail_blankName() throws Exception {

        var dto = new ProductCreateRequestDto("", 15000, "https://image.com/item.jpg");

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name").value("상품명은 필수입니다."));
    }

    @Test
    @DisplayName("[API] 상품 등록 실패 - 상품명 15자 초과")
    void createProduct_fail_nameTooLong() throws Exception {

        var dto = new ProductCreateRequestDto("1234567890123456", 15000, "https://image.com/item.jpg");

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name").value("최대 15자까지 가능합니다."));
    }

    @Test
    @DisplayName("[API] 상품 등록 실패 - 가격 없음")
    void createProduct_fail_priceMissing() throws Exception {

        var dto = new ProductCreateRequestDto("초콜릿", null, "https://image.com/item.jpg");

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.price").value("가격은 필수입니다."));
    }

    @Test
    @DisplayName("[API] 상품 등록 실패 - 가격 음수")
    void createProduct_fail_negativePrice() throws Exception {

        var dto = new ProductCreateRequestDto("초콜릿", -1000, "https://image.com/item.jpg");

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.price").value("가격은 0 이상이어야 합니다."));
    }

    @Test
    @DisplayName("[API] 상품 등록 실패 - 이미지 URL 없음")
    void createProduct_fail_imageUrlMissing() throws Exception {

        var dto = new ProductCreateRequestDto("초콜릿", 1000, "");

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.imageUrl").value("이미지 URL은 필수입니다."));
    }

    @Test
    @DisplayName("[API] 상품 등록 실패 - 유효하지 않은 이미지 URL")
    void createProduct_fail_invalidImageUrl() throws Exception {

        var dto = new ProductCreateRequestDto("초콜릿", 1000, "image.com/item.jpg");

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.imageUrl").value("유효한 이미지 URL이 아닙니다."));
    }

}
