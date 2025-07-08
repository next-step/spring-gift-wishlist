package gift.entity.member.value;

import java.util.Objects;
import java.util.regex.Pattern;

public record MemberEmail(String value) {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    public MemberEmail {
        Objects.requireNonNull(value, "이메일은 필수 입력값입니다.");
        if (!EMAIL_REGEX.matcher(value).matches()) {
            throw new IllegalArgumentException("유효한 이메일 형식이 아닙니다.");
        }
    }
}
