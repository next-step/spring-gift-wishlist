package gift.auth;

import gift.member.domain.Member;
import gift.member.domain.RoleType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isApiRequest = request.getRequestURI().startsWith("/api");

        Member member = (Member) request.getAttribute("member");

        if(member == null || member.getRole() != RoleType.ADMIN) {
            return handleAuthError(response, isApiRequest, "관리자 권한이 필요합니다.");
        }

        return true;
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
