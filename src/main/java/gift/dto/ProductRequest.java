package gift.dto;

import gift.entity.Product;
import gift.validator.ValidProductName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
        @NotBlank(message = "상품 이름은 비워둘 수 없습니다.")
        @ValidProductName
        String name,

        @Positive(message = "상품 가격은 0보다 커야 합니다.")
        Integer price,

        @NotBlank(message = "이미지 URL은 비워둘 수 없습니다.")
        String imageUrl
) {
    public Product toEntity() {
        return new Product(name, price, imageUrl);
    }
}