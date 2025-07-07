package gift.controller.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.ItemRequest;
import gift.dto.LoginResponse;
import gift.dto.MemberLoginRequest;
import gift.dto.MemberRegisterRequest;
import gift.entity.Item;
import gift.repository.ItemRepository;
import gift.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberService memberService;

    private Item testItem1;

    @BeforeEach
    void setUp() {
        testItem1 = itemRepository.save(new Item(null, "API 테스트 상품", 1000, "api.jpg"));
        itemRepository.save(new Item(null, "두 번째 상품", 2000, "api2.jpg"));
    }

    @Test
    @DisplayName("API - 전체 상품 목록 조회 테스트")
    void getAllItems() throws Exception {
        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("API - ID로 상품 조회 성공 테스트")
    void getItemById_Success() throws Exception {
        mockMvc.perform(get("/api/products/" + testItem1.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testItem1.getId()))
            .andExpect(jsonPath("$.name").value("API 테스트 상품"));
    }

    @Test
    @DisplayName("API - 상품 등록 성공 테스트")
    void createItem_Success() throws Exception {
        ItemRequest itemRequest = new ItemRequest("새 API 상품", 5000, "new_api.jpg");
        String requestBody = objectMapper.writeValueAsString(itemRequest);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("API - 상품 수정 성공 테스트")
    void updateItem_Success() throws Exception {
        ItemRequest itemRequest = new ItemRequest("수정된 API 상품", 1500, "updated.jpg");
        String requestBody = objectMapper.writeValueAsString(itemRequest);

        mockMvc.perform(put("/api/products/" + testItem1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("수정된 API 상품"));
    }

    @Test
    @DisplayName("API - 상품 삭제 성공 테스트")
    void deleteItem_Success() throws Exception {
        mockMvc.perform(delete("/api/products/" + testItem1.getId()))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("인증 토큰 없이 상품 수정 API 호출 시 401 에러 발생")
    void updateItem_Without_Token_Fails() throws Exception {
        Item savedItem = itemRepository.save(new Item(null, "수정될 상품", 1000, "image.jpg"));
        ItemRequest request = new ItemRequest("수정된 상품", 2000, "new_image.jpg");

        mockMvc.perform(put("/api/products/" + savedItem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("유효한 인증 토큰으로 상품 수정 API 호출 성공")
    void updateItem_With_Token_Succeeds() throws Exception {
        memberService.register(new MemberRegisterRequest("auth@example.com", "password"));
        LoginResponse loginResponse = memberService.login(new MemberLoginRequest("auth@example.com", "password"));
        String token = loginResponse.token();

        Item savedItem = itemRepository.save(new Item(null, "수정될 상품", 1000, "image.jpg"));
        ItemRequest request = new ItemRequest("수정된 상품", 2000, "new_image.jpg");

        mockMvc.perform(put("/api/products/" + savedItem.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("수정된 상품"));
    }

    @Test
    @DisplayName("USER 권한으로 '카카오' 포함 상품 등록 시 403 에러 발생")
    void createKakaoItem_With_UserRole_Fails() throws Exception {
        LoginResponse loginResponse = memberService.login(new MemberLoginRequest("user@example.com", "user1234"));
        String userToken = loginResponse.token();
        ItemRequest request = new ItemRequest("카카오프렌즈 인형", 30000, "kakao_doll.jpg");
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/products")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("ADMIN 권한으로 '카카오' 포함 상품 등록 성공")
    void createKakaoItem_With_AdminRole_Succeeds() throws Exception {
        LoginResponse loginResponse = memberService.login(new MemberLoginRequest("admin@example.com", "admin1234"));
        String adminToken = loginResponse.token();
        ItemRequest request = new ItemRequest("카카오프렌즈 인형", 30000, "kakao_doll.jpg");
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/products")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated());
    }
}