package gift.dto;

import gift.validator.ProductPolicy;
import gift.validator.ProductPolicyProvider;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@ProductPolicy
public record ProductRequestDto (
    @NotBlank(message = "상품 이름은 필수로 입력 해야합니다.")
    @Size(max = 15, message = "상품 이름은 공백 포함 최대 15자까지 입력 할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]*$",
        message = "특수기호는 ( ) [ ] + - & / _ 만 허용됩니다.")
    String name,
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    @Max(value = 1000000000000L, message = "가격이 비정상적으로 큰 값입니다.")
    long price,
    String imageUrl,
    boolean merchandiserApproved
) implements ProductPolicyProvider {
}
