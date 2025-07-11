package gift.user.domain;

import gift.auth.PasswordUtil;

import java.util.Base64;
import java.util.UUID;

public class User {
    private UUID id;
    private String email;
    private String password;
    private String salt;

    public User(UUID id, String email, String password, String salt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
    }

    public boolean isEqualToPassword(String password) throws Exception{
        byte[] salt = Base64.getDecoder().decode(this.salt);
        String hashedPassword = PasswordUtil.encryptPassword(password, salt);
        return this.password.equals(hashedPassword);
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }
}
