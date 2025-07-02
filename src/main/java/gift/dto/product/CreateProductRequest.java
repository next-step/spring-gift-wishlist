package gift.dto.product;

import jakarta.validation.constraints.*;

public record CreateProductRequest(
        @NotBlank(message = "이름은 필수 입력 값입니다.")
        @Size(max = 15, message = "상품은 공백을 포함하여 15자까지 입력할 수 있습니다.")
        @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣()\\[\\]+\\-&/_\\s]*$", message = "특수문자는 ( ), [ ], +, -, &, /, _ 만 허용됩니다.")
        @Pattern(regexp = "^(?!.*카카오).*$", message = "[카카오]를 포함하는 이름은 문의가 필요합니다.")
        String name,

        @NotNull(message = "가격은 필수 입력 값입니다.")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        @Max(value = 100_000_000, message = "가격은 1억원 이하여야 합니다.")
        Integer price,

        @NotNull(message = "수량은 필수 입력 값입니다.")
        @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
        @Max(value = 100_000_000, message = "수량은 1억개 이하여야 합니다.")
        Integer quantity
) {

    public static CreateProductRequest empty() {
        return new CreateProductRequest(null, null, null);
    }
}
