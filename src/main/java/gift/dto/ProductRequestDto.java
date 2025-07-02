package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class ProductRequestDto {

    @NotBlank(message = "상품 이름은 비어 있을 수 없습니다.")
    @Size(max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다.")
    @Pattern(
            regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$",
            message = "상품 이름에 허용되지 않은 문자가 포함되어 있습니다."
    )
    private String name;

    @PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
    private int price;

    @NotBlank(message = "이미지 URL은 비어 있을 수 없습니다.")
    private String imageUrl;

    public ProductRequestDto() {
    }

    public ProductRequestDto(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }

    public int getPrice() { return price; }

    public String getImageUrl() { return imageUrl; }

    public void setName(String name) { this.name = name; }

    public void setPrice(int price) { this.price = price; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
