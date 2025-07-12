package gift.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;

  public JwtAuthenticationFilter(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    String path = request.getRequestURI();

    if (path.startsWith("/api/members") || path.startsWith("/api/products")) {
      filterChain.doFilter(request, response);
      return;
    }

    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing or malformed");
      return;
    }

    String token = authHeader.substring(7);

    if (!jwtProvider.validateToken(token)) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
      return;
    }

    Long memberId = jwtProvider.getMemberId(token);
    request.setAttribute("memberId", memberId);

    filterChain.doFilter(request, response);
  }
}
