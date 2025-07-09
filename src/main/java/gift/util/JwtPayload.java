package gift.util;

import gift.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

public record JwtPayload(Claims claims) {
    public Member toMember() {
        String idString = claims.get("sub", String.class);
        String email = claims.get("email", String.class);

        if (idString.isEmpty() || email.isEmpty()) {
            throw new JwtException("NOT valid Token");
        }

        Long id = Long.parseLong(idString);
        return new Member(id, email, "");
    }
}
