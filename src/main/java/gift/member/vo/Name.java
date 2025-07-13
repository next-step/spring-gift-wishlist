package gift.member.vo;

public class Name {
    private final String value;

    public Name(String value) {
        check(value);
        this.value = value;
    }

    private void check(String name) {
        if (name == null || name.length() < 3) {
            throw new IllegalArgumentException("이름은 3자 이상이어야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
