package gift.common.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtAuthenticationFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        // 로그인 & 회원가입은 필터 적용 X
        if (path.equals("/api/members/login") || path.equals("/api/members/register")) {
            chain.doFilter(request, response);
            return;
        }

        String header = httpRequest.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.getSubject(token);
                String role = jwtUtil.getRole(token);
                Long memberId =  jwtUtil.getMemberId(token);

                httpRequest.setAttribute("email", email);
                httpRequest.setAttribute("role", role);
                httpRequest.setAttribute("memberId", memberId);

                chain.doFilter(request, response);
                return;
            }
        }

        // 인증 실패: 401 Unauthorized 응답
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write("{\"message\": \"Unauthorized: 유효한 JWT 토큰이 필요합니다.\"}");
    }
}
