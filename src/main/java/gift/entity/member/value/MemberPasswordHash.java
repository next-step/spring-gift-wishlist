package gift.entity.member.value;

import java.util.Objects;

public record MemberPasswordHash(String password) {

    public MemberPasswordHash {
        Objects.requireNonNull(password, "비밀번호 해시는 null일 수 없습니다.");
        if (password.length() < 60) {
            throw new IllegalArgumentException("유효한 비밀번호 해시가 아닙니다.");
        }
    }
}
