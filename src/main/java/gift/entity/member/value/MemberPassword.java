package gift.entity.member.value;

import java.util.Objects;

public record MemberPassword(String password) {

    public static final int PASSWORD_LENGTH = 64;

    public MemberPassword {
        Objects.requireNonNull(password, "비밀번호는 null일 수 없습니다.");
        if (password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 공백일 수 없습니다.");
        }
        if (password.length() < PASSWORD_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 " + PASSWORD_LENGTH + " 이하여야 합니다.");
        }
    }
}
