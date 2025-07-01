package gift.dto;

import jakarta.validation.constraints.*;

public class ProductRequestDto {

    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 15, message = "상품명은 공백 포함 최대 15자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$",
            message = "상품명으로 허용되지 않는 특수 문자가 존재합니다. 가능한 특수문자: (), [], +, -, &, /, _")
    private final String name;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0원 이상입니다.")
    private final Long price;

    private final String imageUrl;
  
    public ProductRequestDto(Long price, String name, String imageUrl) {
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
