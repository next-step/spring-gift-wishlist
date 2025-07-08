package gift.interceptor;


import gift.auth.JwtTokenHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenHandler jwtTokenHandler;

    public AuthInterceptor(final JwtTokenHandler jwtTokenHandler) {
        this.jwtTokenHandler = jwtTokenHandler;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return jwtTokenHandler.verifyToken(token);
    }

}
