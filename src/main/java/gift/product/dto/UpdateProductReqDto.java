package gift.product.dto;

import gift.product.validation.NoKakao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UpdateProductReqDto(
    @NotBlank(message = "상품명은 필수값입니다")
    @Size(max = 15)
    @Pattern(
        regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-&/_가-힣]*$",
        message = "상품 이름에는 허용되지 않은 특수문자가 포함되어 있습니다."
    )
    @NoKakao
    String name,

    @PositiveOrZero(message = "가격은 0원 이상이어야 합니다")
    @NotNull(message = "가격은 필수값입니다")
    Integer price,

    @Size(max = 500, message = "상품설명은 최대 500자까지 입력할 수 있습니다")
    @NotBlank(message = "상품 설명은 필수값입니다")
    String description,

    @NotBlank(message = "이미지 URL은 필수입니다.")
    @Pattern(regexp = "^(http|https)://.*$", message = "올바른 URL 형식이어야 합니다")
    String imageUrl
) {

}
