package gift;

import gift.auth.LoginMemberArgumentResolver;
import gift.dto.api.WishResponseDto;
import gift.entity.Member;
import gift.exception.InvalidAuthorizationHeaderException;
import gift.exception.MissingAuthorizationHeaderException;
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

import java.util.List;

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

    @MockitoBean
    private WishService wishService;

    @MockitoBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @BeforeEach
    void setUpResolver() throws Exception {
        // supportsParameter를 true로 해서, 리졸버가 호출되도록 허용
        given(loginMemberArgumentResolver.supportsParameter(any()))
                .willReturn(true);

        Member mockMember = new Member(1L, "test@example.com", "password123");

        // resolveArgument를 원하는 Member 객체로 반환하도록 설정
        given(loginMemberArgumentResolver.resolveArgument(
                any(), any(), any(), any()))
                .willAnswer(invocation -> {
                    NativeWebRequest webRequest = invocation.getArgument(2);
                    String header = webRequest.getHeader("Authorization");

                    if (header == null) {
                        throw new MissingAuthorizationHeaderException("Authorization 헤더가 필요합니다.");
                    }
                    if (!header.startsWith("Bearer ")) {
                        throw new InvalidAuthorizationHeaderException("Authorization 헤더 형식이 올바르지 않습니다.");
                    }

                    return mockMember;
                });
    }

    @Test
    @DisplayName("GET /api/wishes – 정상 조회 시 200 OK + JSON 배열 반환")
    void list_withValidAuth_shouldReturnWishItems() throws Exception {
        // given
        var sample = new WishResponseDto(
                1L, "초콜릿", 1000, "https://image.com/choco.png", 2
        );
        given(wishService.getWishListForMember(any()))
                .willReturn(List.of(sample));

        // when & then
        mockMvc.perform(get("/api/wishes")
                        .header("Authorization", "Bearer dummy-token")  // resolver가 mock이면 토큰 내용 무관
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].name").value("초콜릿"))
                .andExpect(jsonPath("$[0].price").value(1000))
                .andExpect(jsonPath("$[0].imageUrl").value("https://image.com/choco.png"))
                .andExpect(jsonPath("$[0].quantity").value(2));
    }

    @Test
    @DisplayName("GET /api/wishes – 아이템 없으면 빈 배열")
    void list_empty_shouldReturnEmptyArray() throws Exception {
        given(wishService.getWishListForMember(any()))
                .willReturn(List.of());

        mockMvc.perform(get("/api/wishes")
                        .header("Authorization", "Bearer dummy-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /api/wishes – 여러 아이템 조회")
    void list_multipleItems_shouldReturnAll() throws Exception {
        var item1 = new WishResponseDto(1L,"초콜릿",1000,"https://image.com/choco.png",1);
        var item2 = new WishResponseDto(2L,"사탕",  500,"https://image.com/candy.png",3);
        given(wishService.getWishListForMember(any()))
                .willReturn(List.of(item1, item2));

        mockMvc.perform(get("/api/wishes")
                        .header("Authorization", "Bearer dummy-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].name").value("초콜릿"))
                .andExpect(jsonPath("$[0].price").value(1000))
                .andExpect(jsonPath("$[0].imageUrl").value("https://image.com/choco.png"))
                .andExpect(jsonPath("$[0].quantity").value(1))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].name").value("사탕"))
                .andExpect(jsonPath("$[1].price").value(500))
                .andExpect(jsonPath("$[1].imageUrl").value("https://image.com/candy.png"))
                .andExpect(jsonPath("$[1].quantity").value(3));
    }

    @Test
    @DisplayName("GET /api/wishes – 인증 헤더 없으면 401 Unauthorized")
    void list_withoutAuthHeader_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/wishes"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error")
                        .value("Authorization 헤더가 필요합니다."));
    }

    @Test
    @DisplayName("GET /api/wishes – 잘못된 Authorization 프리픽스")
    void list_badAuthPrefix_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/wishes")
                        .header("Authorization", "Basic abc123"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error")
                        .value("Authorization 헤더 형식이 올바르지 않습니다."));
    }

}
