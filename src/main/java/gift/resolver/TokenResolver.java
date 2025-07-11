package gift.resolver;

import gift.annotation.UserValid;
import gift.dto.UserInfoRequestDto;
import gift.exception.TokenUnauthorizedException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenResolver implements HandlerMethodArgumentResolver {
    private final String jwtKey;

    public TokenResolver(@Value("${jwt_key}") String jwtKey) {
        this.jwtKey = jwtKey;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserValid.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        // 헤더 검사
        String authHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더가 없거나 Bearer 토큰이 아닙니다.");
        }

        String token = authHeader.substring("Bearer ".length());

        // JWT 파싱
        Claims claims;
        try {
            claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(jwtKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (Exception e) {
            throw new TokenUnauthorizedException("유효하지 않은 JWT 입니다.");
        }

        return new UserInfoRequestDto(claims.get("id", Long.class), claims.get("role", String.class));
    }
}