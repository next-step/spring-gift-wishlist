package gift.item.dto;

import gift.item.validator.ValidItemName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateItemDto {

    @NotBlank(message = "상품명은 필수입니다")
    @Size(max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다.")
    @ValidItemName
    private String name;

    @NotNull(message = "가격은 필수입니다")
    @Min(value=0, message = "가격은 0 이상이어야 합니다")
    private Integer price;

    @NotBlank(message = "이미지 URL은 필수입니다")
    private String imageUrl;

    protected UpdateItemDto() {}

    public UpdateItemDto(String name, Integer price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

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