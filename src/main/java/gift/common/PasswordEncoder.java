package gift.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public static String encode(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encoded) {
        return ENCODER.matches(rawPassword, encoded);
    }
}
