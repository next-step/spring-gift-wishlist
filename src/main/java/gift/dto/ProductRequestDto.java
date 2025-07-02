package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(
        @NotBlank(message = "상품명은 필수입니다.")
        @Size(max = 15, message = "상품명은 최대 15자까지 입력 가능합니다.")
        @Pattern(
                regexp = "^[a-zA-Z0-9ㄱ-힣 ()\\[\\]+\\-&/_]*$",
                message = "(), [], +, -, $, /, _ 외의 특수문자는 사용할 수 없습니다."
        )
        String name,

        @NotNull(message = "가격은 필수입니다.")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        Integer price,

        @NotBlank(message = "이미지는 필수입니다.")
        String imageUrl) {

}
