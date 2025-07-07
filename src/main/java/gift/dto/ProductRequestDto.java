package gift.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class ProductRequestDto {

    @NotBlank(message = "상품 이름은 비어 있을 수 없습니다")
    @Size(max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다")
    @Pattern(
            regexp = "^[\\p{L}\\p{N} ()\\[\\]+\\-&/_]*$",
            message = "상품 이름에는 허용되지 않은 특수문자가 포함되어 있습니다"
    )
    private final String name;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다")
    private final BigDecimal price;

    @NotBlank(message = "이미지 URL은 비어 있을 수 없습니다")
    private final String imgUrl;

    @AssertTrue(message = "\"카카오\"가 포함된 상품 이름은 사용할 수 없습니다")
    public boolean isNameNotAllowed() {
        if (name == null) return true;
        return !name.contains("카카오");
    }

    public ProductRequestDto(String name, BigDecimal price, String imgUrl) {
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
