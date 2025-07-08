package gift.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthUtil {

    public static Credentials parse(String header) {
        if (header == null || !header.startsWith("Basic ")) {
            throw new IllegalArgumentException("Invalid Basic Authorization header");
        }
        String base64 = header.substring(6);
        String decoded = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
        String[] parts = decoded.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid Basic credentials format");
        }
        return new Credentials(parts[0], parts[1]);
    }

    public record Credentials(String email, String password) {

    }
}
