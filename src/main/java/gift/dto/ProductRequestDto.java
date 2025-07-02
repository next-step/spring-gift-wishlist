package gift.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(
        @Size(max = 15, message = "상품명은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]*$",
                message = "상품명에 허용되지 않는 특수문자가 있습니다. 사용가능: ( ), [ ], +, -, &, /, _")
        String name,
        Integer price,
        String imageUrl
) {

}
