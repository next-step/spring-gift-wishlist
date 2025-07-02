package gift.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(
        @NotNull(message = "이름은 필수입니다.")
        @Size(min = 1, max = 15, message = "상품 이름은 1자 이상 15자 이하여야 합니다.")
        String name,
        @NotNull(message = "가격은 필수입니다.")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        @Max(value = 9_999_999_999L, message = "가격은 9,999,999,999 이하여야합니다.")
        Long price,
        @Size(max = 1000, message = "이미지 URL은 1,000자 이하여야 합니다.")
        String imageUrl) {
}
