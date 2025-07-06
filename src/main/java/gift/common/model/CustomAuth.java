package gift.common.model;

import gift.entity.UserRole;
import java.util.Set;

public record CustomAuth(
    Long userId,
    Set<UserRole> authorities
) {

}

