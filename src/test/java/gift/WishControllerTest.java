package gift;

import gift.auth.LoginMemberArgumentResolver;
import gift.dto.api.WishRequestDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.exception.InvalidAuthorizationHeaderException;
import gift.exception.MissingAuthorizationHeaderException;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.service.WishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.NativeWebRequest;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WishService wishService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @MockitoBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    Member member; // 매 테스트마다 사용할 로그인 회원

    @BeforeEach
    void setUp() {
        // (1) 테스트용 회원 저장
        member = memberRepository.save(new Member("test@example.com", "pwd1234"));

        // (2) ArgumentResolver 목 스텁
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willAnswer(invocation -> {
                    NativeWebRequest req = invocation.getArgument(2);
                    String header = req.getHeader("Authorization");
                    if (header == null) {
                        // 헤더 누락 → 401 Unauthorized
                        throw new MissingAuthorizationHeaderException("Authorization 헤더가 필요합니다.");
                    }
                    if (!header.startsWith("Bearer ")) {
                        // 헤더 형식 오류 → 401 Unauthorized
                        throw new InvalidAuthorizationHeaderException("Authorization 헤더 형식이 올바르지 않습니다.");
                    }
                    return member; // 항상 로그인 성공
                });
    }

    @Test
    @DisplayName("GET /api/wishes – 정상 조회 시 200 OK + JSON 배열 반환")
    void list_withValidAuth_shouldReturnWishItems() throws Exception {
        // given
        var sampleProduct = new Product("초콜릿", 1000, "https://image.com/choco.png");
        Product saved = productRepository.save(sampleProduct);
        var sampleWishRequestDto = new WishRequestDto(saved.getId(), 2);
        wishService.addWishItemForMember(member, sampleWishRequestDto);

        // when & then
        mockMvc.perform(get("/api/wishes")
                        .header("Authorization", "Bearer dummy-token")  // resolver가 mock이면 토큰 내용 무관
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productId").value(saved.getId()))
                .andExpect(jsonPath("$[0].name").value("초콜릿"))
                .andExpect(jsonPath("$[0].price").value(1000))
                .andExpect(jsonPath("$[0].imageUrl").value("https://image.com/choco.png"))
                .andExpect(jsonPath("$[0].quantity").value(2));
    }

    @Test
    @DisplayName("GET /api/wishes – 아이템 없으면 빈 배열")
    void list_empty_shouldReturnEmptyArray() throws Exception {
        mockMvc.perform(get("/api/wishes")
                        .header("Authorization", "Bearer dummy-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /api/wishes – 여러 아이템 조회")
    void list_multipleItems_shouldReturnAll() throws Exception {
        var sampleProduct1 = new Product("초콜릿",1000,"https://image.com/choco.png");
        var sampleProduct2 = new Product("사탕",  500,"https://image.com/candy.png");
        Product savedSampleProduct1 = productRepository.save(sampleProduct1);
        Product savedSampleProduct2 = productRepository.save(sampleProduct2);
        var item1 = new WishRequestDto(savedSampleProduct1.getId(),1);
        var item2 = new WishRequestDto(savedSampleProduct2.getId(),3);
        wishService.addWishItemForMember(member, item1);
        wishService.addWishItemForMember(member, item2);

        mockMvc.perform(get("/api/wishes")
                        .header("Authorization", "Bearer dummy-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId").value(savedSampleProduct1.getId()))
                .andExpect(jsonPath("$[0].name").value("초콜릿"))
                .andExpect(jsonPath("$[0].price").value(1000))
                .andExpect(jsonPath("$[0].imageUrl").value("https://image.com/choco.png"))
                .andExpect(jsonPath("$[0].quantity").value(1))
                .andExpect(jsonPath("$[1].productId").value(savedSampleProduct2.getId()))
                .andExpect(jsonPath("$[1].name").value("사탕"))
                .andExpect(jsonPath("$[1].price").value(500))
                .andExpect(jsonPath("$[1].imageUrl").value("https://image.com/candy.png"))
                .andExpect(jsonPath("$[1].quantity").value(3));
    }

    @Test
    @DisplayName("GET /api/wishes – 인증 헤더 없으면 401 Unauthorized")
    void list_withoutAuthHeader_unauthorized() throws Exception {
        mockMvc.perform(get("/api/wishes"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error")
                        .value("Authorization 헤더가 필요합니다."));
    }

    @Test
    @DisplayName("GET /api/wishes – 잘못된 Authorization 프리픽스")
    void list_badAuthPrefix_unauthorized() throws Exception {
        mockMvc.perform(get("/api/wishes")
                        .header("Authorization", "Basic abc123"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error")
                        .value("Authorization 헤더 형식이 올바르지 않습니다."));
    }

}
