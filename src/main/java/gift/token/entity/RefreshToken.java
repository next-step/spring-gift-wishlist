package gift.token.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class RefreshToken {

    public static final Duration TTL = Duration.ofDays(365);

    private final String token;
    private final UUID memberUuid;
    private final LocalDateTime createdAt;
    private final LocalDateTime expirationDate;

    public RefreshToken(UUID memberUuid) {
        this.token = UUID.randomUUID().toString().replace("-", "");
        this.memberUuid = memberUuid;
        this.createdAt = LocalDateTime.now();
        this.expirationDate = this.createdAt.plusDays(RefreshToken.TTL.toDays());
    }

    public String getToken() {
        return token;
    }

    public UUID getMemberUuid() {
        return memberUuid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
}
