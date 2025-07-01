package gift.dto.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductCreateRequestDto {

    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 15, message = "최대 15자까지 가능합니다.")
    @Pattern(
            regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$",
            message = "유효한 특수문자 ( '( )', '[ ]', '+', '-', '&', '/', '_' ) 가 아닙니다."
    )
    private String name;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;

    @Pattern(
        regexp = "^(http|https)://.*$",
        message = "유효한 이미지 URL이 아닙니다."
    )
    @NotBlank(message = "이미지 URL은 필수입니다.")
    private String imageUrl;

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
