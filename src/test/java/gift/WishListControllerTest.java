package gift;

import gift.auth.LoginMemberArgumentResolver;
import gift.controller.WishListController;
import gift.domain.Member;
import gift.enums.Role;
import gift.exception.UnauthorizedException;
import gift.service.WishListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(WishListController.class)
public class WishListControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WishListService wishListService;

    @MockitoBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    private final Member mockMember = new Member(1L, "test@email.com", "encoded_pw", Role.ROLE_USER);

    @BeforeEach
    void setUp() throws Exception {
        // 인증 리졸버가 member 반환하도록 설정
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(mockMember);

        // 서비스 결과는 더미로 세팅
        given(wishListService.findAllWishSummaryByMemberId(anyLong()))
                .willReturn(List.of());
    }

    @Test
    void 로그인한_시용자가_위시리스트_조회시_200반환() throws Exception {
        mockMvc.perform(get("/wishes")
                        .header("Authorization", "Bearer mock-token")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 로그인하지_않은_사용자가_위시리스트_조회시_401반환() throws Exception {
        doThrow(new UnauthorizedException("인증되지 않은 사용자입니다", "wishlist-api"))
                .when(loginMemberArgumentResolver)
                .resolveArgument(
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any()
                );

        mockMvc.perform(get("/wishes")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
