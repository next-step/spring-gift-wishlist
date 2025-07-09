package gift.filter;

import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.token.JwtTokenProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Pattern;

import java.io.IOException;
import java.util.Optional;

// 1. extracting JWT Token from the request and validating it
public class JwtTokenFilter implements Filter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = jwtTokenProvider.resolveToken(httpRequest);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            httpRequest.setAttribute("role", jwtTokenProvider.getRole(token));
        } else {
            httpRequest.setAttribute("role", null);
        }
        chain.doFilter(request, response);
    }
}
