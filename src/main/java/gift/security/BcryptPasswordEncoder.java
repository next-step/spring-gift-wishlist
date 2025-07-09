package gift.security;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class BcryptPasswordEncoder {
  public String encode(String rawPassword) {
    return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
  }

  public boolean matches(String rawPassword, String hashedPassword) {
    return BCrypt.checkpw(rawPassword, hashedPassword);
  }

}
