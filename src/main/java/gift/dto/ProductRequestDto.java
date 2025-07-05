package gift.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;


public record ProductRequestDto(
        @NotBlank(message = "상품 이름은 필수입니다.")
        @Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15글자까지 입력할 수 있습니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣ㄱ-ㅎ\\s()\\[\\]+\\-&/_]*$", message = "사용할 수 없는 특수문자가 포함되어 있습니다. ( ), [ ], +, -, &, /, _ 만 사용 가능합니다.")
        String name,

        Long price,
        String imageUrl
) {
}