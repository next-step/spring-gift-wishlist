package gift.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record ProductRequestDto(
        @NotBlank(message = "상품 이름은 필수입니다.")
        @Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15글자까지 입력할 수 있습니다.")
        String name,

        Long price,
        String imageUrl
) {
}