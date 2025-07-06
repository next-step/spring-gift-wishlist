package gift.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {

    private PasswordEncoder() {
        // 객체 생성을 방지하기 위한 private 생성자
    }

    public static String encode(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 null이거나 비어있을 수 없습니다.");
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("비밀번호 인코딩에 실패했습니다. SHA-256 알고리즘을 사용할 수 없습니다.", e);
        }
    }

    public static Boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            throw new IllegalArgumentException("비밀번호와 인코딩된 비밀번호는 null일 수 없습니다.");
        }
        try {
            String hashedRawPassword = encode(rawPassword);
            return hashedRawPassword.equals(encodedPassword);
        } catch (Exception e) {
            throw new RuntimeException("비밀번호 비교 중 오류가 발생했습니다.", e);
        }
    }
}
