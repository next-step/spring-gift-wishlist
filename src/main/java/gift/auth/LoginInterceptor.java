package gift.auth;

import gift.member.domain.Member;
import gift.member.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public LoginInterceptor(JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isApiRequest = request.getRequestURI().startsWith("/api");

        String token = extractToken(request, isApiRequest);

        if (token == null) {
            return handleAuthError(response, isApiRequest, "인증 정보가 없습니다. 다시 로그인해주세요");
        }

        if (!jwtUtil.validateToken(token)) {
            return handleAuthError(response, isApiRequest, "인증 정보가 유효하지 않거나 만료되었습니다. 다시 로그인해주세요.");
        }

        String email = jwtUtil.getEmail(token);
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            return handleAuthError(response, isApiRequest, "사용자를 찾을 수 없습니다.");
        }

        request.setAttribute("member", member.get());
        request.setAttribute("isLoggedIn", Boolean.TRUE);

        return true;
    }

    private String extractToken(HttpServletRequest request, boolean isApiRequest) {
        if (isApiRequest) {
            String authHeader = request.getHeader("Authorization");
            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        }
        else {
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("jwt-token".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
        }
        return null;
    }

    private boolean handleAuthError(HttpServletResponse response, boolean isApiRequest, String msg) throws IOException {
        if (isApiRequest) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            String jsonMsg = String.format("{\"error\": \"%s\"}", msg);
            response.getWriter().write(jsonMsg);
        }
        else {
            String encodedMsg = URLEncoder.encode(msg, StandardCharsets.UTF_8);
            response.sendRedirect("/members/login?error=true&message=" + encodedMsg);
        }
        return false;
    }
}
