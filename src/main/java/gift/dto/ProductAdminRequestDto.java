package gift.dto;

import jakarta.validation.constraints.*;

public record ProductAdminRequestDto(
    @NotBlank(message = "상품 이름은 필수입니다.")
    @Size(max = 15, message = "상품 이름은 최대 15자까지 입력 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-\\&/_ ]{1,15}$", message = "상품 이름에는 일부 특수문자 ( ), [ ], +, -, &, /, _ 만 사용할 수 있습니다.")
    String name,

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    Integer price,

    String imageUrl,

    boolean kakaoConfirmed
) {}
