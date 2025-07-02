package gift.product.dto;

import jakarta.validation.constraints.*;

public record ProductUpdateRequest(
        @NotBlank(message = "상품 이름은 비워둘 수 없습니다.")
        @Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
        @Pattern(
                regexp = "^[a-zA-Z0-9가-힣\\s\\(\\)\\[\\]\\+\\-\\&\\/_]*$",
                message = "상품 이름에 허용되지 않는 특수문자가 포함되어 있습니다. ( ), [ ], +, -, &, /, _ 만 사용 가능합니다."
        )
        @Pattern(
                regexp = "^(?!.*카카오).*$",
                message = "상품 이름에 '카카오'는 담당 MD와 협의된 경우에만 사용할 수 있습니다."
        )
        String name,

        @NotNull(message = "가격은 비워둘 수 없습니다.")
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        @Max(value = 2147483647, message = "가격은 2147483647원 이하이어야 합니다.")
        Long price,

        @NotBlank(message = "이미지 URL은 비워둘 수 없습니다.")
        String imageUrl) {
}
