package gift.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record ProductCreateRequestDto(
        @NotBlank(message = "상품 이름은 필수입니다.")
        @Length(max = 15, message = "상품 이름은 최대 15자까지만 입력 가능합니다.")
        @Pattern(regexp = "[0-9a-zA-Zㄱ-ㅎ가-힣 ()\\[\\]+\\-&/_]+", message = "상품 이름은 한글, 영문, 숫자, 공백, 특수문자((), [], +, -, &, /, _)만 사용할 수 있습니다.")
        String name,
        @NotNull(message = "상품 가격은 필수입니다.")
        @PositiveOrZero(message = "상품 가격은 0 이상이어야 합니다.")
        Long price,
        @URL(message = "유효한 URL 형식이어야 합니다.")
        String imageUrl
) {

}
