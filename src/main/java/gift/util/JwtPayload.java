package gift.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

public class JwtPayload {

    private final Long id;

    public JwtPayload(Claims claims) {
        String idString = claims.get("sub", String.class);
        String email = claims.get("email", String.class);

        if (idString.isEmpty() || email.isEmpty()) {
            throw new JwtException("NOT valid Token");
        }

        id = Long.parseLong(idString);
    }

    public Long getId() {
        return id;
    }
}
