package gift.auth;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter implements Filter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Long memberId = jwtProvider.extractMemberId(token);
                httpRequest.setAttribute("memberId", memberId);
            } catch (Exception e) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }
        } else if (requiresAuth(httpRequest)) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header required");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean requiresAuth(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.startsWith("/api/members/register") || uri.startsWith("/api/members/login") || uri.startsWith("/api/product")) {
            return false;
        }
        return uri.startsWith("/api");
    }

}
