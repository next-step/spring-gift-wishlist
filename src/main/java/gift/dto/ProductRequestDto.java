package gift.dto;

import jakarta.validation.constraints.Size;

public class ProductRequestDto {

    @Size(min = 1, max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다.")
    private String name;
    private Integer price;
    private String imageUrl;


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
