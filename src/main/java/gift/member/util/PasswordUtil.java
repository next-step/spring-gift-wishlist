package gift.member.util;


import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

@Component
public final class PasswordUtil {
    private PasswordUtil() {}

    public static String getSalt(){
        SecureRandom radom = new SecureRandom();
        byte[] salt = new byte[16];
        radom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String salt){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] digest = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(digest);
        } catch(NoSuchAlgorithmException e){
            throw new RuntimeException("비밀번호 해싱 오류", e);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("생성할 관리자 계정의 이메일을 입력하세요: ");
        String email = scanner.nextLine();

        System.out.print("생성할 관리자 계정의 비밀번호를 입력하세요: ");
        String rawPassword = scanner.nextLine();

        scanner.close();

        PasswordUtil passwordUtil = new PasswordUtil();

        String salt = passwordUtil.getSalt();
        String hashedPassword = passwordUtil.hashPassword(rawPassword, salt);

        System.out.println("아래 INSERT 문을 복사하여 'data.sql' 파일에 붙여넣으세요.\n");

        String sql = String.format(
                "INSERT INTO member (email, salt, password, role) VALUES ('%s', '%s', '%s', 'ADMIN');",
                email,
                salt,
                hashedPassword
        );
        System.out.println(sql);
    }
}
