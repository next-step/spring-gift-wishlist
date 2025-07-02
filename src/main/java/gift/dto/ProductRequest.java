package gift.dto;

import jakarta.validation.constraints.*;

public record ProductRequest(

        Long categoryId,

        @NotBlank(message = "상품 이름은 필수입니다.")
        @Size(max = 15, message = "상품 이름은 최대 15자까지 입력 가능합니다.")
        @Pattern(
                regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]\\+\\-\\&/_]*$",
                message = "상품 이름에 허용되지 않는 특수 문자가 포함되어 있습니다."
        )
        String name,

        @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
        int price,

        @NotBlank(message = "이미지 URL은 필수입니다.")
        @Size(max = 500, message = "이미지 URL은 500자 이하여야 합니다.")
        String imageUrl

) {}
