package gift.util;

import gift.exception.custom.InvalidBearerAuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.stereotype.Component;

@Component
public class BearerAuthUtil {

    private final JwtUtil jwtUtil;

    public BearerAuthUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * “Bearer <token>” 헤더를 파싱해서 Claims 포함 Jws 객체 리턴
     *
     * @throws InvalidBearerAuthException 헤더가 없거나 형식이 잘못된 경우
     */
    public Jws<Claims> parse(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new InvalidBearerAuthException("잘못된 Bearer Autorization 오류");
        }
        String token = header.substring(7).trim();
        return jwtUtil.parseToken(token);
    }
}
