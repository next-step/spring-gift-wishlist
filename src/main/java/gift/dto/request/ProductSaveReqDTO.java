package gift.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductReqDTO(
    @NotBlank(
        message = "상품 이름을 입력해주세요."
    )
    @Size(
        min = 1,
        max = 15,
        message = "상품 이름은 공백을 포함하여 최대 15글자까지 입력할 수 있습니다."
    )
    @Pattern.List({
        @Pattern(
            regexp = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\\(\\)\\[\\]\\+\\-\\&/_ ]*$",
            message = "상품 이름은 한글, 영어, 숫자, 특수문자(( ), [ ], +, -, &, /, _) 외 "
                + "다른 문자가 들어갈 수 없습니다."
        ),
        @Pattern(
            regexp = "^(?!.*카카오).*$",
            message = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다."
        )
    })
    String name,

    @NotNull(
        message = "상품 가격을 입력해주세요."
    )
    @PositiveOrZero(
        message = "올바른 상품 가격을 입력해주세요."
    )
    @Max(
        value = 2147483647,
        message = "상품 가격은 2,147,483,647원을 초과할 수 없습니다."
    )
    Integer price,

    String imageURL
) {

}
