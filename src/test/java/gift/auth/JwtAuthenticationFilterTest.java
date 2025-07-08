package gift.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("유효한 JWT 토큰이 있으면 memberId를 request에 저장한다")
    void validToken_shouldSetMemberId() throws ServletException, IOException {
        // given
        String token = "valid.jwt.token";
        Long expectedMemberId = 5L;

        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getMemberId(token)).thenReturn(expectedMemberId);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        assertThat(request.getAttribute("memberId")).isEqualTo(expectedMemberId);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Authorization 헤더가 없으면 memberId를 설정하지 않는다")
    void noAuthHeader_shouldNotSetMemberId() throws ServletException, IOException {
        // given
        // Authorization 헤더 없음

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        assertThat(request.getAttribute("memberId")).isNull();
        verify(jwtTokenProvider, never()).validateToken(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Bearer 접두사가 없으면 memberId를 설정하지 않는다")
    void noBearerPrefix_shouldNotSetMemberId() throws ServletException, IOException {
        // given
        request.addHeader("Authorization", "invalid.token.format");

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        assertThat(request.getAttribute("memberId")).isNull();
        verify(jwtTokenProvider, never()).validateToken(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("유효하지 않은 토큰이면 401을 반환하고 필터 체인을 진행하지 않는다")
    void invalidToken_shouldReturn401AndStopFilter() throws ServletException, IOException {
        // given
        String token = "invalid.jwt.token";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED); // 401
        verify(jwtTokenProvider, never()).getMemberId(anyString());
        verify(filterChain, never()).doFilter(request, response); // 더 이상 필터 체인 진행되지 않음
    }

    @Test
    @DisplayName("회원가입 경로는 필터를 적용하지 않는다")
    void registerPath_shouldSkipFilter() {
        // given
        request.setServletPath("/api/members/register");

        // when
        boolean shouldNotFilter = jwtAuthenticationFilter.shouldNotFilter(request);

        // then
        assertThat(shouldNotFilter).isTrue();
    }

    @Test
    @DisplayName("로그인 경로는 필터를 적용하지 않는다")
    void loginPath_shouldSkipFilter() {
        // given
        request.setServletPath("/api/members/login");

        // when
        boolean shouldNotFilter = jwtAuthenticationFilter.shouldNotFilter(request);

        // then
        assertThat(shouldNotFilter).isTrue();
    }

    @Test
    @DisplayName("일반 API 경로는 필터를 적용한다")
    void normalApiPath_shouldApplyFilter() {
        // given
        request.setServletPath("/api/products");

        // when
        boolean shouldNotFilter = jwtAuthenticationFilter.shouldNotFilter(request);

        // then
        assertThat(shouldNotFilter).isFalse();
    }
}