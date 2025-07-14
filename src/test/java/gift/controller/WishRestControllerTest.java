package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.CreateWishRequest;
import gift.dto.LoginMember;
import gift.entity.Member;
import gift.entity.Product;
import gift.jwt.JwtTokenProvider;
import gift.repository.MemberRepository;
import gift.service.WishService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class WishRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WishService wishService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("위시 추가 성공")
    void addWishSuccess() throws Exception {
        // given
        Member testMember = Member.withId(1L, "aran@email.com", "1234");
        String token = "valid_token";

        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getEmailFromToken(token)).thenReturn(testMember.getEmail());
        when(memberRepository.findByEmail(testMember.getEmail())).thenReturn(
                Optional.of(testMember));

        CreateWishRequest request = new CreateWishRequest(1L, 10L);

        // when
        MvcResult result = mockMvc.perform(post("/api/wishes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("위시 추가 실패")
    void addWishFail() throws Exception {
        // given
        CreateWishRequest request = new CreateWishRequest(1L, 10L);
        String invalidToken = "invalid_token";

        when(jwtTokenProvider.validateToken(invalidToken)).thenReturn(false);

        // when
        MvcResult result = mockMvc.perform(post("/api/wishes")
                        .header("Authorization", "Bearer " + invalidToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("위시 조회 성공")
    void getWishSuccess() throws Exception {
        // given
        Member testMember = Member.withId(1L, "aran@email.com", "1234");
        String token = "valid_token";

        // Mock JWT 토큰 검증 및 멤버 조회
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getEmailFromToken(token)).thenReturn(testMember.getEmail());
        when(memberRepository.findByEmail(testMember.getEmail())).thenReturn(
                Optional.of(testMember));

        // WishService가 반환할 상품 리스트 생성
        List<Product> wishList = Collections.singletonList(new Product(10L, "gamja", "gam.com"));
        when(wishService.getAllWish(testMember.getId())).thenReturn(wishList);

        // when
        MvcResult result = mockMvc.perform(get("/api/wishes")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        String responseContains = result.getResponse().getContentAsString();
        assertThat(responseContains).contains("gamja");
    }

    @Test
    @DisplayName("위시 조회 실패")
    void getWishFail() throws Exception {
        String invalidToken = "invalid_token";

        when(jwtTokenProvider.validateToken(invalidToken)).thenReturn(false);

        MvcResult result = mockMvc.perform(get("/api/wishes")
                        .header("Authorization", "Bearer " + invalidToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("위시 삭제 성공")
    void deleteWishSuccess() throws Exception {
        // given
        Member testMember = Member.withId(1L, "aran@email.com", "1234");
        String token = "valid_token";

        // Mock JWT 토큰 검증 및 멤버 조회
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getEmailFromToken(token)).thenReturn(testMember.getEmail());
        when(memberRepository.findByEmail(testMember.getEmail())).thenReturn(
                Optional.of(testMember));

        // 삭제는 반환값 없음

        // when
        MvcResult result = mockMvc.perform(delete("/api/wishes/{productId}", 10L)
                        .header("Authorization", "Bearer " + token))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("위시 삭제 실패")
    void deleteWishFail() throws Exception {
        // given
        String invalidToken = "invalid_token";

        when(jwtTokenProvider.validateToken(invalidToken)).thenReturn(false);

        // when
        MvcResult result = mockMvc.perform(delete("/api/wishes/{productId}", 10L)
                        .header("Authorization", "Bearer " + invalidToken))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


}
