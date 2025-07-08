// src/main/java/gift/util/BearerAuthUtil.java
package gift.util;

import gift.exception.InvalidBearerAuthExeption;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class BearerAuthUtil {

    public static Jws<Claims> parse(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new InvalidBearerAuthExeption();
        }
        String token = header.substring(7);
        return JwtUtil.validate(token);
    }
}
