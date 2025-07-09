package gift.jwt.filter;

import gift.domain.Role;
import gift.global.MySecurityContextHolder;
import gift.global.exception.JWTValidateException;
import gift.global.exception.NotFoundEntityException;
import gift.jwt.JWTUtil;
import gift.jwt.JWTValidator;
import gift.member.dto.AuthMember;
import gift.member.service.MemberService;
import io.jsonwebtoken.Claims;
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

    private final JWTValidator jwtValidator;

    public ViewFilter(JWTValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;

        String requestURI = request.getRequestURI();

        if(passRequestURI(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        String redirectUrl = "/login?redirect=" + URLEncoder.encode(requestURI, StandardCharsets.UTF_8);

        try {
            AuthMember authMember = jwtValidator.validateJWT(request);
            MySecurityContextHolder.set(authMember);
            chain.doFilter(request, response);
        } catch (JWTValidateException e) {
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
