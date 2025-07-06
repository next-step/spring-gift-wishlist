package gift.dto;

import gift.entity.RoleType;
import jakarta.validation.constraints.NotNull;

public class RoleRequestDto {

    @NotNull(message = "역할은 필수 입력입니다.")
    private final RoleType role;

    public RoleRequestDto(RoleType role) {
        this.role = role;
    }

    public RoleType getRole() {
        return role;
    }
}
