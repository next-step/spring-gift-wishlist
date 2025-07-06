package gift.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.Role;
import gift.global.MySecurityContextHolder;
import gift.jwt.JWTUtil;
import gift.member.dto.AuthMember;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class JWTFilter implements Filter {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public JWTFilter(JWTUtil jwtUtil, ObjectMapper objectMapper) {
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

        String token = request.getHeader("Authorization");

        if (token == null) {
            writeUnauthorizedResponse(response, "토큰이 존재하지 않습니다.");
            return;
        }

        if (!token.startsWith("Bearer ")) {
            writeUnauthorizedResponse(response, "잘못된 토큰 형식입니다.");
            return;
        }

        String accessToken = token.substring("Bearer ".length());


        if(jwtUtil.isExpired(accessToken)) {
            writeUnauthorizedResponse(response, "만료된 토큰 입니다..");
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
