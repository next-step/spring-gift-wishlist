package gift;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.JwtProvider;
import gift.auth.LoginMemberArgumentResolver;
import gift.controller.WishController;
import gift.domain.Member;
import gift.dto.request.WishRequest;
import gift.dto.response.WishMsgResponse;
import gift.service.MemberService;
import gift.service.WishService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WishController.class)
@Import(WishControllerTest.TestConfig.class)
public class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WishService wishService;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @MockitoBean
    private JwtProvider jwtProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("위시리스트 상품 추가에 성공한다")
    @Test
    void 위시리스트_추가_성공() throws Exception {
        given(wishService.add(any(Member.class), any(WishRequest.class)))
                .willReturn(new WishMsgResponse("위시리스트에 추가되었습니다."));

        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(new Member(1L, "test@email.com", "1234"));

        mockMvc.perform(post("/api/wishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "productId": 1
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/wishes/1"))
                .andExpect(jsonPath("$.message").value("위시리스트에 추가되었습니다."));
    }

    @DisplayName("인증되지 않은 사용자가 위시리스트를 추가하려 하면 401 Unauthorized 에러가 발생한다")
    @Test
    void 위시리스트_추가_실패_인증없음() throws Exception {
        mockMvc.perform(post("/api/wishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "productId": 1
                        }
                    """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("인증 정보가 유효하지 않습니다."));
    }

    @TestConfiguration
    static class TestConfig implements WebMvcConfigurer {
        @Autowired
        private LoginMemberArgumentResolver loginMemberArgumentResolver;

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(loginMemberArgumentResolver);
        }
    }

}
