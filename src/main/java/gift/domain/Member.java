package gift.domain;

import gift.policy.EmailPolicy;
import gift.policy.PasswordPolicy;

public class Member {

    private final Long id;
    private final String email;
    private final String password;

    private Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static Member withId(Long id, String email, String password) {
        validateEmail(email);
        validatePassword(password);

        return new Member(id, email, password);
    }

    // 회원가입용 - ID 없이 생성
    public static Member register(String email, String password) {
        validateEmail(email);
        validatePassword(password);

        return new Member(null, email, password);
    }

    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        if (!email.matches(EmailPolicy.EMAIL_REGEX)) {
            throw new IllegalArgumentException(EmailPolicy.EMAIL_RULE_MESSAGE);
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        if (!password.matches(PasswordPolicy.PASSWORD_REGEX)) {
            throw new IllegalArgumentException(PasswordPolicy.PASSWORD_RULE_MESSAGE);
        }
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}