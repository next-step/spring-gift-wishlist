package gift.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberUpdateRequest(
        @NotNull
        Long id,
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 4, message = "비밀번호는 최소 4자리 이상이어야 합니다.")
        String password
) {
    private static final Long DEFAULT_ID = null;
    private static final String DEFAULT_PASSWORD = "";
    public static MemberUpdateRequest getEmpty() {
        return new MemberUpdateRequest(DEFAULT_ID, DEFAULT_PASSWORD);
    }
}
