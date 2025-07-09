package gift.security;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class PasswordEncoder {
    private final String salt = "kimhaejoong1";

    public String encode(String password) {
        try {
            String saltedPassword = password + salt;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(saltedPassword.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean matches(String password, String encodedPassword) {
        return encode(password).equals(encodedPassword);
    }
}
