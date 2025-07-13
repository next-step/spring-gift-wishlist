package gift.controller.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.LoginResponse;
import gift.dto.MemberLoginRequest;
import gift.dto.MemberRegisterRequest;
import gift.dto.WishRequest;
import gift.dto.WishUpdateRequest;
import gift.dto.WishResponse;
import gift.entity.Item;
import gift.entity.Member;
import gift.repository.ItemRepository;
import gift.repository.MemberRepository;
import gift.service.MemberService;
import gift.service.WishService;
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
class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private WishService wishService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MemberRepository memberRepository;

    private String userToken;
    private Member loginMember;
    private Item testItem1;

    @BeforeEach
    void setUp() {
        memberService.register(new MemberRegisterRequest("wish@example.com", "password"));
        LoginResponse loginResponse = memberService.login(new MemberLoginRequest("wish@example.com", "password"));
        userToken = loginResponse.token();
        loginMember = memberRepository.findByEmail("wish@example.com").get();
        testItem1 = itemRepository.save(new Item(null, "테스트 상품 1", 10000, "test1.jpg"));
    }

    @Test
    @DisplayName("위시리스트에 새 상품 추가 및 조회 테스트")
    void addAndGetWishes() throws Exception {
        WishRequest wishRequest = new WishRequest(testItem1.getId(), 2);
        mockMvc.perform(post("/api/wishes")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wishRequest)))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/wishes")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].product.id").value(testItem1.getId()))
            .andExpect(jsonPath("$[0].quantity").value(2));
    }

    @Test
    @DisplayName("중복된 상품 추가 시 409 에러 발생")
    void addWish_Fail_When_Duplicate() throws Exception {
        wishService.addWish(new WishRequest(testItem1.getId(), 1), loginMember);

        mockMvc.perform(post("/api/wishes")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new WishRequest(testItem1.getId(), 1))))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("위시리스트 상품 수량 변경 및 삭제 테스트")
    void updateAndDeleteWish() throws Exception {
        WishResponse addedWish = wishService.addWish(new WishRequest(testItem1.getId(), 1), loginMember);
        Long wishId = addedWish.wishId();

        mockMvc.perform(patch("/api/wishes/" + wishId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new WishUpdateRequest(10))))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/wishes")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
            .andExpect(jsonPath("$[0].quantity").value(10));

        mockMvc.perform(delete("/api/wishes/" + wishId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/wishes")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
            .andExpect(jsonPath("$", hasSize(0)));
    }
}