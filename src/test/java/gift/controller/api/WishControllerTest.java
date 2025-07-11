package gift.controller.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.ItemRequest;
import gift.dto.LoginResponse;
import gift.dto.MemberLoginRequest;
import gift.dto.MemberRegisterRequest;
import gift.dto.WishRequest;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ItemRepository itemRepository;

    private String userToken;
    private Item testItem;

    @BeforeEach
    void setUp() {
        memberService.register(new MemberRegisterRequest("wish@example.com", "password"));
        LoginResponse loginResponse = memberService.login(new MemberLoginRequest("wish@example.com", "password"));
        userToken = loginResponse.token();

        testItem = itemRepository.save(new Item(null, "테스트 상품", 10000, "test.jpg"));
    }

    @Test
    @DisplayName("위시리스트에 상품 추가 및 조회 테스트")
    void addAndGetWishes() throws Exception {
        WishRequest wishRequest = new WishRequest(testItem.getId());
        String requestBody = objectMapper.writeValueAsString(wishRequest);

        mockMvc.perform(post("/api/wishes")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/wishes")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].product.id").value(testItem.getId()));
    }

    @Test
    @DisplayName("위시리스트 상품 삭제 테스트")
    void deleteWish() throws Exception {
        WishRequest wishRequest = new WishRequest(testItem.getId());
        String addRequestBody = objectMapper.writeValueAsString(wishRequest);
        mockMvc.perform(post("/api/wishes")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(addRequestBody));

        MvcResult getResult = mockMvc.perform(get("/api/wishes")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
            .andReturn();
        String jsonResponse = getResult.getResponse().getContentAsString();
        Long wishId = objectMapper.readTree(jsonResponse).get(0).get("wishId").asLong();

        mockMvc.perform(delete("/api/wishes/" + wishId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/wishes")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }
}