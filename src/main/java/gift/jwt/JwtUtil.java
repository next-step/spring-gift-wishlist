package gift.jwt;

import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String AUTHORIZATION_KEY = "AUTH";

    private final SecretKey secretKey;
}
