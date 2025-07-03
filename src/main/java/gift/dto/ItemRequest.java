package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import gift.validation.ValidProductName;

public record ItemRequest(

    @ValidProductName
    @NotBlank(message = "상품 이름은 비어 있을 수 없습니다.")
    String name,

    @Min(value = 0, message = "가격은 0보다 작을 수 없습니다.")
    @NotNull(message = "가격은 비어 있을 수 없습니다.")
    int price,

    @NotBlank(message = "이미지 URL은 비어 있을 수 없습니다.")
    String imageUrl
) {

}