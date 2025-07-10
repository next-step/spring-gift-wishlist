package gift.util;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public class RoleUtil {

    public static String extractRole(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("authClaims");
        return claims.get("role", String.class);
    }
}
