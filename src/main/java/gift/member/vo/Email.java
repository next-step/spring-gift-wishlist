package gift.member.vo;

public class Email {
    private final String value;

    public Email(String value) {
        check(value);
        this.value = value;
    }

    private void check(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
