package giftproject.member.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import giftproject.member.annotation.LoginMember;
import giftproject.member.entity.Member;
import giftproject.member.repository.MemberRepository;
import giftproject.member.util.JwtTokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class LoginMemberArgumentResolverTest {

    @InjectMocks
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MethodParameter methodParameter;

    @Mock
    private ModelAndViewContainer mavContainer;

    @Mock
    private NativeWebRequest webRequest;

    @Mock
    private WebDataBinderFactory binderFactory;

    private final String MOCK_TOKEN = "Bearer mock.jwt.token";
    private final Long MOCK_MEMBER_ID = 1L;
    private final String MOCK_EMAIL = "test@example.com";
    private Member mockMember;

    @BeforeEach
    void setUp() {
        mockMember = new Member(MOCK_MEMBER_ID, MOCK_EMAIL, "encodedPassword");
    }

    @Test
    @DisplayName("supportsParameter: @LoginMember 어노테이션이 있고 Member 타입이면 true 반환")
    void supportsParameter_shouldReturnTrueForLoginMemberAndMemberType() {
        when(methodParameter.hasParameterAnnotation(LoginMember.class)).thenReturn(true);
        when(methodParameter.getParameterType()).thenReturn(Member.class);

        boolean result = loginMemberArgumentResolver.supportsParameter(methodParameter);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("supportsParameter: @LoginMember 어노테이션이 없으면 false 반환")
    void supportsParameter_shouldReturnFalseForNoLoginMemberAnnotation() {
        when(methodParameter.hasParameterAnnotation(LoginMember.class)).thenReturn(false);
        when(methodParameter.getParameterType()).thenReturn(Member.class); // 타입은 Member지만 어노테이션 없음

        boolean result = loginMemberArgumentResolver.supportsParameter(methodParameter);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("supportsParameter: @LoginMember 어노테이션은 있지만 Member 타입이 아니면 false 반환")
    void supportsParameter_shouldReturnFalseForLoginMemberButNotMemberType() {
        when(methodParameter.hasParameterAnnotation(LoginMember.class)).thenReturn(true);
        when(methodParameter.getParameterType()).thenReturn(String.class); // 타입이 String

        boolean result = loginMemberArgumentResolver.supportsParameter(methodParameter);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("resolveArgument: 유효한 토큰으로 Member 객체 반환")
    void resolveArgument_shouldReturnMemberForValidToken() throws Exception {
        when(webRequest.getHeader("Authorization")).thenReturn(MOCK_TOKEN);
        when(jwtTokenProvider.getMemberIdFromToken(anyString())).thenReturn(MOCK_MEMBER_ID);
        when(memberRepository.findById(MOCK_MEMBER_ID)).thenReturn(Optional.of(mockMember));

        Object result = loginMemberArgumentResolver.resolveArgument(methodParameter, mavContainer,
                webRequest, binderFactory);

        assertThat(result).isInstanceOf(Member.class);
        assertThat(((Member) result).getId()).isEqualTo(MOCK_MEMBER_ID);
        assertThat(((Member) result).getEmail()).isEqualTo(MOCK_EMAIL);
    }

    @Test
    @DisplayName("resolveArgument: Authorization 헤더가 없으면 UNAUTHORIZED 예외 발생")
    void resolveArgument_shouldThrowUnauthorizedWhenNoAuthorizationHeader() {
        when(webRequest.getHeader("Authorization")).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                loginMemberArgumentResolver.resolveArgument(methodParameter, mavContainer,
                        webRequest, binderFactory)
        );

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getReason()).isEqualTo("인증 토큰이 누락되었거나 유효하지 않습니다.");
    }

    @Test
    @DisplayName("resolveArgument: 토큰 형식이 'Bearer '로 시작하지 않으면 UNAUTHORIZED 예외 발생")
    void resolveArgument_shouldThrowUnauthorizedWhenInvalidTokenFormat() {
        when(webRequest.getHeader("Authorization")).thenReturn("InvalidToken");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                loginMemberArgumentResolver.resolveArgument(methodParameter, mavContainer,
                        webRequest, binderFactory)
        );

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getReason()).isEqualTo("인증 토큰이 누락되었거나 유효하지 않습니다.");
    }

    @Test
    @DisplayName("resolveArgument: JWT 토큰이 유효하지 않으면 (JwtTokenProvider에서 예외 발생) UNAUTHORIZED 예외 발생")
    void resolveArgument_shouldThrowUnauthorizedWhenJwtTokenInvalid() {
        when(webRequest.getHeader("Authorization")).thenReturn(MOCK_TOKEN);
        when(jwtTokenProvider.getMemberIdFromToken(anyString()))
                .thenThrow(
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                loginMemberArgumentResolver.resolveArgument(methodParameter, mavContainer,
                        webRequest, binderFactory)
        );

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getReason()).isEqualTo("유효하지 않은 JWT 토큰입니다.");
    }

    @Test
    @DisplayName("resolveArgument: memberId로 회원을 찾을 수 없으면 UNAUTHORIZED 예외 발생")
    void resolveArgument_shouldThrowUnauthorizedWhenMemberNotFound() {
        when(webRequest.getHeader("Authorization")).thenReturn(MOCK_TOKEN);
        when(jwtTokenProvider.getMemberIdFromToken(anyString())).thenReturn(MOCK_MEMBER_ID);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                loginMemberArgumentResolver.resolveArgument(methodParameter, mavContainer,
                        webRequest, binderFactory)
        );

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getReason()).isEqualTo("유효하지 않은 사용자 토큰입니다.");
    }
}
