package gift.dto;

import gift.exception.InvalidImageUrlException;
import gift.exception.InvalidNameException;
import gift.exception.InvalidPriceException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(
    @NotBlank(message = "상품명은 입력되어야 합니다.")
    @Size(max = 15, message = "상품명의 최대길이는 15자 입니다.")
    String name,

    @Min(value = 0, message = "최소가격은 0입니다.")
    int price,

    @NotBlank(message = "이미지에 대한 url은 입력되어야 합니다.")
    String imageUrl
) {

}
