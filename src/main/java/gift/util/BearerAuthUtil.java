package gift.util;

import gift.exception.InvalidBearerAuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class BearerAuthUtil {

    public static Jws<Claims> parse(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new InvalidBearerAuthException();
        }
        String token = header.substring(7);
        return JwtUtil.parseToken(token);
    }
}
