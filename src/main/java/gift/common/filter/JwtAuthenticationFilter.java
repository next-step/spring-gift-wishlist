package gift.common.filter;

import gift.common.exception.InvalidAccessTokenException;
import gift.common.exception.InvalidTokenException;
import gift.dto.user.UserInfo;
import gift.service.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String AUTH_HEADER = "Authorization";
    private static final String HEADER_PREFIX = "Bearer ";
    private static final String COOKIE_NAME = "accessToken";

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = null;
            String authHeader = request.getHeader(AUTH_HEADER);
            if (authHeader != null) {
                authHeader = authHeader.trim();
            }

            if (authHeader != null && authHeader.startsWith(HEADER_PREFIX)) {
                jwt = authHeader.substring(HEADER_PREFIX.length());
            }

            if (authHeader == null) {
                if (request.getCookies().length == 0) {
                    throw new InvalidAccessTokenException();
                }
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(COOKIE_NAME)) {
                        String val = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                        if (!val.startsWith(HEADER_PREFIX)) {
                            throw new InvalidTokenException();
                        }
                        jwt = val.substring(HEADER_PREFIX.length());
                        break;
                    }
                }
            }

            if (jwt == null) {
                throw new InvalidAccessTokenException();
            }

            Claims claims = jwtTokenProvider.validAccessToken(jwt);
            UserInfo userInfo = jwtTokenProvider.getUserInfoFromClaims(claims);
            request.setAttribute("userInfo", userInfo);

        } catch (RuntimeException e) {
            throw new InvalidTokenException(e);
        }

        filterChain.doFilter(request, response);
    }

}
