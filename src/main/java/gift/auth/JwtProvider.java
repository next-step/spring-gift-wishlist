package gift.auth;

import org.springframework.beans.factory.annotation.Value;

public class JwtProvider {
    
    @Value(value = "${jwt.secret}")
    private String secretKey;
}
