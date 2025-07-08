package gift.entity.member.value;

import java.util.Objects;

public record MemberPasswordHash(String value) {

    public MemberPasswordHash {
        Objects.requireNonNull(value, "비밀번호 해시는 null일 수 없습니다.");
        if (value.length() < 60) {
            throw new IllegalArgumentException("유효한 비밀번호 해시가 아닙니다.");
        }
    }
}
