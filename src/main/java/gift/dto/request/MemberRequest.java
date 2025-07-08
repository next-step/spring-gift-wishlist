package gift.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberRequest (
        @NotBlank(message = "email을 입력하세요.")
        String email,

        @NotBlank(message = "비밀번호를 입력하세요.")
        String pwd
){}
