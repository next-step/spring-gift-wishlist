package gift.member.dto;

import gift.member.entity.Member;
import java.time.LocalDateTime;
import java.util.UUID;

public record MemberInfo(
        UUID uuid,
        String email,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public MemberInfo(Member member) {
        this(member.getUuid(), member.getEmail(), member.getName(), member.getCreatedAt(),
                member.getUpdatedAt());
    }
}
