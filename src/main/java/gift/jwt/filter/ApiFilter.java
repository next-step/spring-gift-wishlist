package gift.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.Role;
import gift.global.MySecurityContextHolder;
import gift.jwt.JWTUtil;
import gift.member.dto.AuthMember;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class ApiFilter implements Filter {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public ApiFilter(JWTUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;

        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        if(passRequestURI(method, requestURI)) {
            chain.doFilter(request, response);
            return;
        };

        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            writeUnauthorizedResponse(response,"쿠키가 존재하지 않습니다.");
            return;
        }

        String accessToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("Authorization"))
                .findFirst()
                .map(cookie -> cookie.getValue())
                .orElse(null);

        if (accessToken == null) {
            writeUnauthorizedResponse(response, "토큰이 존재하지 않습니다.");
            return;
        }


        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            writeUnauthorizedResponse(response, "만료된 토큰입니다.");
            return;
        }


        try {
            String email = jwtUtil.getEmail(accessToken);
            String role = jwtUtil.getRole(accessToken);
            MySecurityContextHolder.set(new AuthMember(email, Role.valueOf(role)));
            chain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            writeUnauthorizedResponse(response, "잘못된 토큰 형식입니다.");
        }
        finally {
            MySecurityContextHolder.clear();
        }

    }

    private boolean passRequestURI(String method, String requestURI) {
        if (requestURI.equals("/login")) return true;
        if (requestURI.equals("/api/members/login"))  return true;
        if (requestURI.equals("/api/members") && "POST".equalsIgnoreCase(method)) return true;

        return false;
    }

    private void writeUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Map.of("message", message)));
    }
}
