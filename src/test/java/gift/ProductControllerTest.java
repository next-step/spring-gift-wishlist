package gift;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.api.ProductCreateRequestDto;
import gift.entity.ApprovedProduct;
import gift.entity.Product;
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
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value(dto.getName()))
            .andExpect(jsonPath("$.price").value(dto.getPrice()))
            .andExpect(jsonPath("$.imageUrl").value(dto.getImageUrl()));
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

    @Test
    @DisplayName("[API] 상품 등록 실패 - JSON 파싱 오류(400) - 가격에 문자를 넣는 경우")
    void createProduct_fail_invalidJsonFormat() throws Exception {
        // 1) 잘못된 JSON 바디 직접 작성
        String badJson = "{"
            + "\"name\": \"초콜릿\","
            + "\"price\": \"과자\","               // 숫자여야 할 필드에 문자열
            + "\"imageUrl\": \"https://image.com/item.jpg\""
            + "}";

        // 2) perform 요청을 통해 GlobalExceptionHandler.handleHttpMessageNotReadable(...) 동작 검증
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("'price' 필드는 Integer 형식이어야 합니다."));
    }

    @Test
    @DisplayName("[API] 상품 조회 성공 - 200 OK")
    void getProduct_success() throws Exception {
        // 준비: 상품 저장
        var saved = productRepository.save(new Product("초콜릿", 1000, "https://image.com/item.jpg"));
        Long id = saved.getId();

        // 수행 & 검증
        mockMvc.perform(get("/api/products/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value("초콜릿"))
            .andExpect(jsonPath("$.price").value(1000))
            .andExpect(jsonPath("$.imageUrl").value("https://image.com/item.jpg"));
    }

    @Test
    @DisplayName("[API] 상품 조회 실패 - 없는 ID(404)")
    void getProduct_notFound() throws Exception {
        mockMvc.perform(get("/api/products/{id}", 9999L)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("상품을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("[API] 상품 조회 실패 - 잘못된 ID 형식(400)")
    void getProduct_invalidIdFormat() throws Exception {
        mockMvc.perform(get("/api/products/{id}", "abc")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("'id' 파라미터는 long 형식이어야 합니다."));
    }

}
