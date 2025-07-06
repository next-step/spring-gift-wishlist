package gift.jwt.filter;

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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ViewFilter implements Filter {

    public ViewFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private final JWTUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;

        String requestURI = request.getRequestURI();

        if(passRequestURI(requestURI)) {
            chain.doFilter(request, response);
            return;
        };

        String redirectUrl = "login?redirect=" + URLEncoder.encode(requestURI, StandardCharsets.UTF_8);

        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            response.sendRedirect(redirectUrl);
            return;
        }

        String accessToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("Authorization"))
                .findFirst()
                .map(cookie -> cookie.getValue())
                .orElse(null);


        if (accessToken == null) {
            response.sendRedirect(redirectUrl);
            return;
        }



        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            response.sendRedirect(redirectUrl);
            return;
        }


        try {
            String email = jwtUtil.getEmail(accessToken);
            String role = jwtUtil.getRole(accessToken);
            MySecurityContextHolder.set(new AuthMember(email, Role.valueOf(role)));
            chain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            response.sendRedirect(redirectUrl);
        }
        finally {
            MySecurityContextHolder.clear();
        }
    }

    private boolean passRequestURI(String requestURI) {
        if (requestURI.startsWith("/api") || requestURI.equals("/login")) return true;
        return false;
    }
}
