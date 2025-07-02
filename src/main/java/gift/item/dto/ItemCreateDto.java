package gift.item.dto;

import gift.common.validation.OnlyPermittedSymbols;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItemCreateDto(
    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 15, message = "상품명은 공백포함 최대 15자까지 입력할 수 있습니다.")
    @OnlyPermittedSymbols
    String name,
    Integer price,
    String imageUrl
) {

}
