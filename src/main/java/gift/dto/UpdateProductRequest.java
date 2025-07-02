package gift.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateProductRequest(

    @NotEmpty(message = "상품명이 입력되지 않았습니다.")
    @Length(max = 15, message = "상품명은 최대 15자까지 입력 가능합니다.")
    @Pattern(
        regexp = "^[0-9a-zA-Z가-힣()\\s\\[\\]+\\-&/_]*$",
        message = "( ), [ ], +, -, &, /, _ 외의 특수문자는 사용하실 수 없습니다."
    )
    String name,

    @NotNull(message = "상품 가격이 입력되지 않았습니다.")
    @Min(value = 0, message = "상품 가격은 음수가 될 수 없습니다.")
    Integer price,

    @NotEmpty(message = "상품 이미지가 입력되지 않았습니다.")
    String imageUrl
) {
}
