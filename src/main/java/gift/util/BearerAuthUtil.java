package gift.util;

public final class BearerAuthUtil {

    private BearerAuthUtil() {
    }

    /**
     * 토큰 문자열을 받아 Bearer 인증 헤더 형태로 반환합니다. 예) "Bearer abc.def.ghi"
     */
    public static String createHeader(String token) {
        return "Bearer " + token;
    }

    /**
     * Authorization 헤더로부터 Bearer 토큰만 추출합니다.
     *
     * @throws IllegalArgumentException 형식이 올바르지 않을 때
     */
    public static String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Bearer authorization header");
        }
        return authorizationHeader.substring(7).trim();
    }
}
