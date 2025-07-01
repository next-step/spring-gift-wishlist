package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public class ProductRequestDto {
    @NotBlank(message = "상품 이름을 반드시 입력해야 합니다.")
    private String name;

    @NotNull(message = "상품 가격을 반드시 입력해야 합니다.")
    @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
    private int price;

    @NotBlank(message = "상품 이미지 URL을 반드시 입력해야 합니다.")
    @URL(message = "유효한 URL 형식이 아닙니다.")
    private String imageUrl;

    public ProductRequestDto() {
    }

    public ProductRequestDto(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }
}
