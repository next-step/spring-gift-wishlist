package gift.Jwt;

import gift.exception.userException.NeedAuthorizedException;
import gift.exception.userException.UnauthorizedException;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {

    private final JwtUtil jwtUtil;

    public TokenUtils(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String extractToken(String authHeader) {
        return authHeader.replace("Bearer ", "").trim();
    }

    public void validateToken(String token) {
        if (!jwtUtil.validate(token)) {
            throw new UnauthorizedException();
        }
    }

    public String extractEmail(String token) {
        Claims claims = jwtUtil.getClaims(token);
        return claims.get("email", String.class);
    }

    public boolean hasRole(String token, String expectedRole) {
        Claims claims = jwtUtil.getClaims(token);
        String role = claims.get("role", String.class);
        return expectedRole.equals(role);
    }

    public boolean requireAdmin(String token) {
        if (!hasRole(token, "ADMIN")) {
            throw new NeedAuthorizedException();
        }
        return true;
    }
}
