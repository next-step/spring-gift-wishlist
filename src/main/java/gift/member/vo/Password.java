package gift.member.vo;

public class Password {
    private final String value;

    public Password(String value) {
        check(value);
        this.value = value;
    }

    private void check(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }

    public boolean matches(String inputValue) {
        return value.equals(inputValue);
    }
}
