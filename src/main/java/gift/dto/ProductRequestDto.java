package gift.dto;

import jakarta.validation.constraints.*;
public class ProductRequestDto {
    @NotBlank(message = "상품 이름은 필수 입력 사항입니다.")
    @Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
    @Pattern(
            regexp = "^[\\p{L}\\p{N} ()\\[\\]+\\-\\&/_]*$",
            message = "상품 이름에는 허용되지 않은 특수문자가 포함되어 있습니다."
    )
    private String name;

    @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
    private int price;

    @NotBlank(message = "이미지 URL은 비어 있을 수 없습니다.")
    private String imageUrl;

    public ProductRequestDto() {
    }




    public ProductRequestDto(String name, int price, String imageUrl){
        this.name = name;
        this.price = price;
        this.imageUrl=imageUrl;
    }
    public String getName(){
        return name;
    }
    public int getPrice(){
        return price;
    }
    public String getImageUrl(){
        return imageUrl;
    }

}
