package gift.entity.vo;

public class Email {

    private final String value;

    public Email(String value) {
        check(value);
        this.value = value;
    }

    private void check(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
    }

    public String value() {
        return value;
    }
}
