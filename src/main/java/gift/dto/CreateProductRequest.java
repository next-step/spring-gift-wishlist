package gift.dto;

import gift.validation.ProductNamePattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(

    @NotBlank(message = "상품명이 입력되지 않았습니다.")
    @Size(max = 15, message = "상품명은 최대 15자까지 입력 가능합니다.")
    @ProductNamePattern
    String name,

    @NotNull(message = "상품 가격이 입력되지 않았습니다.")
    @Min(value = 0, message = "상품 가격은 음수가 될 수 없습니다.")
    Integer price,

    @NotBlank(message = "상품 이미지가 입력되지 않았습니다.")
    String imageUrl
) {
}
