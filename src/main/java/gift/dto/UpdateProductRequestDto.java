package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductRequestDto(
        Long id,

        @NotBlank(message = "상품 이름은 비어 있을 수 없습니다.")
        @Size(max = 15, message = "상품 이름은 공백 포함 최대 15자까지 입력할 수 있습니다.")
        String name,

        @NotNull(message = "가격은 필수 입력입니다.")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        Long price,

        @NotBlank(message = "상품 이미지는 비어 있을 수 없습니다.")
        String imageUrl
) {
}
