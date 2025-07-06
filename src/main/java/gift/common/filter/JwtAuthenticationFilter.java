package gift.common.filter;

import gift.common.exception.InvalidAccessTokenException;
import gift.common.exception.InvalidTokenException;
import gift.service.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String AUTH_HEADER = "Authorization";
    private static final String HEADER_PREFIX = "Bearer ";

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTH_HEADER);

        try {
            if (authHeader == null || !authHeader.startsWith(HEADER_PREFIX)) {
                throw new InvalidAccessTokenException("Authorization 헤더가 유효하지 않습니다.");
            }
            final String jwt = authHeader.substring(7);

            jwtTokenProvider.validAccessToken(jwt);

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | InvalidAccessTokenException e) {
            throw new InvalidTokenException(e);
        }

        filterChain.doFilter(request, response);
    }

}
