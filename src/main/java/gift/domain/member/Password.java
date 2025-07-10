package gift.domain.member;

public class Password {
    private final String value;

    public Password(String value) {
        if (value == null || value.length() < 8) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상이어야 합니다.");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "********";
    }
}
