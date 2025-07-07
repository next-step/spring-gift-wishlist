package gift.token.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class RefreshToken {

    public static final Duration TTL = Duration.ofDays(365);

    @NotBlank
    private final String token;
    
    @NotNull
    private final UUID memberUuid;
    
    @NotNull
    private final LocalDateTime createdAt;
    
    @NotNull
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
