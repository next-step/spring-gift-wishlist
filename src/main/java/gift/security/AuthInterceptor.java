package gift.security;

import gift.entity.Member;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    public AuthInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        System.out.println("AuthInterceptor: "
                + request.getMethod()
                + " "
                + request.getRequestURI());
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return false;
        }
        String token = header.substring(7);
        Long memberId;
        try {
            memberId = memberService.parseTokenAndGetMemberId(token);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false;
        }
        Member member = memberService.findById(memberId);
        request.setAttribute("loginMember", member);
        return true;
    }
}