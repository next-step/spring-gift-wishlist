package gift.entity.member.value;

import java.util.Objects;

public record MemberId(Long value) {
    public MemberId {
        Objects.requireNonNull(value, "회원 ID는 null일 수 없습니다.");
        if (value <= 0) {
            throw new IllegalArgumentException("회원 ID는 양수여야 합니다.");
        }
    }
}
