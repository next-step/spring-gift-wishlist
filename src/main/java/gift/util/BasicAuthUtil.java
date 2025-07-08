package gift.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class BasicAuthUtil {

    private BasicAuthUtil() {
        // 유틸 클래스이므로 인스턴스화 방지
    }

    /**
     * 이메일과 비밀번호를 Base64로 인코딩한 Basic 인증 헤더 값을 반환합니다. 예) "Basic YWJjQGV4YW1wbGUuY29tOnBhc3N3b3Jk"
     */
    public static String encodeCredentials(String email, String password) {
        String credentials = email + ":" + password;
        String base64 = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        return "Basic " + base64;
    }

    /**
     * Authorization 헤더로부터 Basic 인증 정보를 디코딩하여 [email, password] 배열로 반환합니다.
     *
     * @throws IllegalArgumentException 형식이 올바르지 않거나 디코딩 실패 시
     */
    public static String[] decodeCredentials(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
            throw new IllegalArgumentException("Invalid Basic authorization header");
        }
        String base64 = authorizationHeader.substring(6).trim();
        byte[] decoded = Base64.getDecoder().decode(base64);
        String[] parts = new String(decoded, StandardCharsets.UTF_8).split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid Basic credentials format");
        }
        return parts;  // [0]=email, [1]=password
    }
}
