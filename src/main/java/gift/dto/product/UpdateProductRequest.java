package gift.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateProductRequest(
        @NotBlank(message = "이름은 필수 입력 값입니다.") String name,
        @NotNull(message = "가격은 필수 입력 값입니다.") Integer price,
        @NotNull(message = "수량은 필수 입력 값입니다.") Integer quantity
) {
    public static UpdateProductRequest from(ProductManageResponse response) {
        return new UpdateProductRequest(response.name(), response.price(), response.quantity());
    }
}
