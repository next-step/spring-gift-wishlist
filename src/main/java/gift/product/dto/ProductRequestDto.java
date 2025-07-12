package gift.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(
        @NotBlank(message = "상품명은 필수입니다.")
        @Size(max = 15, message = "상품명은 최대 15자까지 입력할 수 있습니다.")
        @Pattern(
                regexp = "^[\\p{L}0-9 ()\\[\\]+\\-&/_]+$",
                message = "상품 이름에 허용되지 않는 문자가 포함되어 있습니다."
        )
        @Pattern(
                regexp = "^(?!.*카카오).*$",
                message = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다."
        )
        String name,

        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        int price,

        String imageUrl
) { }
