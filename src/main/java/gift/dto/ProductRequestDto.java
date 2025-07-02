package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(
    @NotBlank(message = "상품명은 입력되어야 합니다.")
    @Size(max = 15, message = "상품명의 최대길이는 15자 입니다.")
    @Pattern(
        regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_\\s]*$",
        message = "상품명에 (), [], +, -, &, /, _ 는 가능합니다."
    )
    String name,

    @Min(value = 0, message = "최소가격은 0입니다.")
    int price,

    @NotBlank(message = "이미지에 대한 url은 입력되어야 합니다.")
    String imageUrl,

    Boolean kakaoApproval
) {

}
